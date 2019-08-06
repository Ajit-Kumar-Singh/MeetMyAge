package mma.services;

import java.util.List;

import data.model.message.GroupMessage;
import mma.listeners.GroupMessageDatasetListener;

public interface GroupMessageService {
    List<GroupMessage> loadAllGroupMessages(long pGroupId, int pStartIndex, int pEndIndex);
    void saveGroupMessageInternal(GroupMessage pGroupMessage);
    void saveIncomingGroupMessage(GroupMessage pGroupMessage);
    List<GroupMessage> loadAllGroupMessages(long pGroupId, int pStartIndex, int pEndIndex, GroupMessageDatasetListener pListener);
    void unregisterDatasetListener(GroupMessageDatasetListener pGroupMessageDatasetListener);
}
