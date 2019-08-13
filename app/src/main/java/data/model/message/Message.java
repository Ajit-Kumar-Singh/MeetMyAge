package data.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private String type;

    @SerializedName("sender")
    @Expose
    private String sender;

    private boolean incomingMessage;

    @SerializedName("receiver")
    @Expose
    private String receiver;

    @SerializedName("senderType")
    @Expose
    private String senderType;

    public Message(Type pType) {
        type = pType != null ? pType.name() : null;
    }

    public String getType() {
        return type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public boolean isIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(boolean incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public enum Type {
        GroupMessage;
    }
}
