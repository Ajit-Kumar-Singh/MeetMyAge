package mma;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import data.SessionManagementUtil;
import data.model.Group;
import data.model.Profile;
import data.model.message.Message;
import data.model.message.MessageParcel;
import mma.services.factory.ServiceFactory;

public class SmackChatManager {
    private static final String HOST = "localhost";
    private AbstractXMPPConnection connection;

    public static SmackChatManager getInstance() {
        return InstanceProvider.INSTACE;
    }

    private static final class InstanceProvider {
        private static  final SmackChatManager INSTACE = new SmackChatManager();
    }

    public AbstractXMPPConnection getConnection() {
        synchronized (this) {
            if (connection == null) {
                initiateConnection();
            }
            return connection;
        }
    }

    private void initiateConnection() {
        Profile loggedInUserProfile = SessionManagementUtil.getUserData();
        String loggedInUserEmail = loggedInUserProfile.getProfileEmail();
        String smackUsername = loggedInUserEmail.substring(0, loggedInUserEmail.indexOf("@"));
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(smackUsername, "Welcome1")
                .setHost("10.0.2.2")
                .setServiceName("localhost")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                .setDebuggerEnabled(true)
                .setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })// to view what's happening in detail
                .build();
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        connection = new XMPPTCPConnection(config);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setReplyToUnknownIqDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        try {
            connection.setPacketReplyTimeout(30000);
            connection.connect();
            if (!connection.isConnected()) {
                Log.w(SmackChatManager.class.getName(), "Smack Channel is not able to connect");
                throw new RuntimeException("Unable to open Slack Channel");
            }
            Log.w(SmackChatManager.class.getName(), "Smack Channel is opened");
            connection.login();
            if (!connection.isAuthenticated()) {
                throw new RuntimeException("Smack Connection is unable to authenticate user");
            }
            Log.w(SmackChatManager.class.getName(), "Smack Connect established");
        } catch (Exception e) {
            Log.w(SmackChatManager.class.getName(), "Error while Opening Smack Channel", e);
        }
    }

    public ChatManager getChatManager() {
        AbstractXMPPConnection connection = getConnection();
        return ChatManager.getInstanceFor(connection);
    }

    public void sendMessage(Message pMessage) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        MessageParcel messageParcel = new MessageParcel();
        messageParcel.setMessage(pMessage);
        messageParcel.setMessageType(pMessage.getType());
        java.lang.reflect.Type messageType = new TypeToken<List<Group>>() {}.getType();
        gsonBuilder.registerTypeAdapter(messageType, MessageParcel.getJsonDeserializer());
        String messageParcelJson = gsonBuilder.create().toJson(messageParcel);
        ChatManager chatManager = SmackChatManager.getInstance().getChatManager();
        org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
        message.setTo(getJdiUsername(pMessage.getReceiver()));
        message.setFrom(getJdiUsername(pMessage.getSender()));
        message.setBody(messageParcelJson);
        try {
            Chat chat = chatManager.createChat(getJdiUsername(pMessage.getReceiver()));
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            //Store in Unprocessed Chat message table
            Log.w("app", "GroupMessageReceiver Not Able To Send message", e);
        }
    }

        public String getJdiUsername(String pEmailAddress) {
            return pEmailAddress.substring(0, pEmailAddress.indexOf("@")) + "@" + HOST;
        }
}
