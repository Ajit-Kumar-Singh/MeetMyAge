package data.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import data.model.Copiable;
import data.model.Group;

public class GroupMessage extends Message implements Copiable<GroupMessage> {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("groupId")
    @Expose
    private Long groupId;

    @SerializedName("chatMessage")
    @Expose
    private String chatMessage;

    private boolean hideSender;

    private boolean senderSelf;

    public GroupMessage() {
        super(Type.GroupMessage);
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHideSender() {
        return hideSender;
    }

    public void setHideSender(boolean hideSender) {
        this.hideSender = hideSender;
    }

    public boolean isSenderSelf() {
        return senderSelf;
    }

    public void setSenderSelf(boolean senderSelf) {
        this.senderSelf = senderSelf;
    }

    @Override
    public GroupMessage copy() {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setId(this.id);
        groupMessage.setReceiver(this.getReceiver());
        groupMessage.setSender(this.getSender());
        groupMessage.setGroupId(this.groupId);
        groupMessage.setChatMessage(this.chatMessage);
        return groupMessage;
    }
}
