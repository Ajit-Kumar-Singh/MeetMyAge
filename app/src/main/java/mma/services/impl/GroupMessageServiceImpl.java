package mma.services.impl;

import java.util.ArrayList;
import java.util.List;

import data.SessionManagementUtil;
import data.model.Group;
import data.model.Profile;
import data.model.message.GroupMessage;
import mma.SmackChatManager;
import mma.dao.factory.DaoFactory;
import mma.listeners.GroupMessageDatasetListener;
import mma.services.GroupMessageService;
import mma.services.factory.ServiceFactory;

public class GroupMessageServiceImpl implements GroupMessageService {
    private List<GroupMessageDatasetListener> datasetListeners;
    private GroupMessageServiceImpl(){
        datasetListeners = new ArrayList<GroupMessageDatasetListener>();
    }
    public static GroupMessageService getInstance() {
        return InstanceProvider.INSTANCE;
    }
    private static final  class InstanceProvider {
        private static final GroupMessageService INSTANCE = new GroupMessageServiceImpl();
    }
    @Override
    public List<GroupMessage> loadAllGroupMessages(long pGroupId, int pStartIndex, int pEndIndex) {
        return DaoFactory.getInstance().getGroupMessageDao().loadAllGroupMessages(pGroupId, pStartIndex, pEndIndex);
    }

    @Override
    public void saveGroupMessageInternal(GroupMessage pGroupMessage) {
        DaoFactory.getInstance().getGroupMessageDao().createGroupMessage(pGroupMessage);
        Group group = ServiceFactory.getInstance().getGroupService().findGroupById(pGroupMessage.getGroupId());
        if (group.getMembers() != null) {
            SmackChatManager smackChatManager = SmackChatManager.getInstance();
            String loggedInUserEmail = SessionManagementUtil.getProfileEmail();
            for (Profile groupMember : group.getMembers()) {
                GroupMessage messageCopy = pGroupMessage.copy();
                if (!loggedInUserEmail.equals(groupMember.getProfileEmail())) {
                    messageCopy.setReceiver(groupMember.getProfileEmail());
                    smackChatManager.sendMessage(messageCopy);
                }
            }
        }
    }

    @Override
    public void saveIncomingGroupMessage(GroupMessage pGroupMessage) {
        saveGroupMessageInternal(pGroupMessage);
        for (GroupMessageDatasetListener datasetListener : datasetListeners) {
            if (datasetListener.isApplicable(pGroupMessage)) {
                datasetListener.onReceive(pGroupMessage);
            }
        }
    }

    @Override
    public List<GroupMessage> loadAllGroupMessages(long pGroupId, int pStartIndex, int pEndIndex, GroupMessageDatasetListener pListener) {
        if (pListener != null) {
            datasetListeners.add(pListener);
        }
        return loadAllGroupMessages(pGroupId, pStartIndex, pEndIndex);
    }

    @Override
    public void unregisterDatasetListener(GroupMessageDatasetListener pGroupMessageDatasetListener) {
        if (pGroupMessageDatasetListener != null) {
            datasetListeners.remove(pGroupMessageDatasetListener);
        }
    }
}
