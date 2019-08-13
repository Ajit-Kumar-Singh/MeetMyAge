package mma.dao;

import java.util.List;

import data.model.Group;
import data.model.message.GroupMessage;

public interface GroupMessageDao {
    void createGroupMessage(GroupMessage pGroupMessage);
    List<GroupMessage> loadAllGroupMessages(Long pGroupId, int pStartIndex, int pEndIndex);
}
