package mma.receivers.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

import data.SessionManagementUtil;
import data.model.message.GroupMessage;
import data.model.message.MessageParcel;
import mma.SmackChatManager;
import mma.services.factory.ServiceFactory;

public class IncomingGroupMessageReceiver extends OfflineBroadcastReceiver<MessageParcel> {
    private IncomingGroupMessageReceiver() {
        super(Type.GroupMessage);
    }

    public static OfflineBroadcastReceiver<MessageParcel> getInstance() {
        return InstanceProvider.INSTANCE;
    }

    @Override
    public IntentFilter getCriteria() {
        return new IntentFilter(getType().name());
    }

    @Override
    public void onReceive(MessageParcel pMessageParcel) {
        if (pMessageParcel != null) {
            GroupMessage groupMessage = (GroupMessage) pMessageParcel.getMessage();
            ServiceFactory.getInstance().getGroupMessageService().saveIncomingGroupMessage(groupMessage);
        }
    }


    @Override
    public MessageParcel convert(Intent intent) {
        MessageParcel messageParcel = (MessageParcel)intent.getSerializableExtra("messageParcel");
        return !SessionManagementUtil.getProfileEmail().equals(messageParcel.getMessage().getSender()) ? messageParcel : null;
    }

    private static final class InstanceProvider {
        private static final IncomingGroupMessageReceiver INSTANCE = new IncomingGroupMessageReceiver();
    }
}
