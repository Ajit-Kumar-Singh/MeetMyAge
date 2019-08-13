package views.pages;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.meetmyage.com.meetmyageapp.R;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SynchronizationPoint;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatStateManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import Presenter.LoginContract;
import Presenter.LoginPresenterImpl;

/**
 * A login screen that offers login via Facebook
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.view{
    private static String TAG = LoginActivity.class.getSimpleName();
    private LoginContract.Presenter mLoginPresenter;
    private  ProgressBar mProgressDialog;
    private static List<ChatMessageListener> chatMessageListenerList = new ArrayList<ChatMessageListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                101 );
        final boolean[] isAlreadyLoggedIn = {false};
        LoginManager.getInstance().retrieveLoginStatus(this.getApplicationContext(), new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                isAlreadyLoggedIn[0] = true;
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        boolean isLoggedIn = isAlreadyLoggedIn[0];
        if (isLoggedIn) {
            //Open the tabbed layout
            onSuccess();
        }
        else
        {
            mLoginPresenter = new LoginPresenterImpl(this,getApplicationContext());
            setContentView(R.layout.activity_login);
            mProgressDialog = findViewById(R.id.login_progress);
            mProgressDialog.setVisibility(View.GONE);
            final LoginButton loginButton = (LoginButton) findViewById(R.id.facbookSignin);
            mLoginPresenter.fbLoginButtonClicked(loginButton);
        }
    }

    private class MyChatTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("oola", "welcome1")
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

            AbstractXMPPConnection conn1 = new XMPPTCPConnection(config);
            XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
            XMPPTCPConnection.setReplyToUnknownIqDefault(true);
            XMPPTCPConnection.setUseStreamManagementDefault(true);

            try {
                conn1.setPacketReplyTimeout(30000);
                conn1.connect();
                if (conn1.isConnected()) {
                    Log.w("app", "conn done");
                }
                conn1.login();

                if (conn1.isAuthenticated()) {
                    Log.w("app", "Auth done");
                    ChatManager chatManager = ChatManager.getInstanceFor(conn1);
                        chatManager.addChatListener(new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean b) {
                                for (ChatMessageListener chatMessageListener : chatMessageListenerList) {
                                    chat.addMessageListener(chatMessageListener);
                                }
                                Log.w("app", chat.toString());
                            }
                        });
                }
            } catch (Exception e) {
                Log.w("app", e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
        }

    }

    public static void registerChatMessageListeners(ChatMessageListener pChatMessageListener) {
        chatMessageListenerList.add(pChatMessageListener);
    }

    public static void deRegisterChatMessageListeners(ChatMessageListener pChatMessageListener) {
        chatMessageListenerList.remove(pChatMessageListener);
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onCancelLogin() {

    }

    @Override
    public void showProgressBar() {
        mProgressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressDialog.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String msg) {
      //  Toast.makeText()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         mLoginPresenter.onActivityResult(requestCode,resultCode,data);
    }
}