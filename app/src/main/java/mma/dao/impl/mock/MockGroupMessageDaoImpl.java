package mma.dao.impl.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.model.message.GroupMessage;
import mma.dao.GroupMessageDao;

public class MockGroupMessageDaoImpl implements GroupMessageDao {
    private Map<Long, GroupMessage> messageByIdMap;
    private Map<Long, List<GroupMessage>> messageByGroupIdMap;
    private long messageCounter;
    private MockGroupMessageDaoImpl() {
        messageByIdMap = new HashMap<Long, GroupMessage>();
        messageByGroupIdMap = new HashMap<Long, List<GroupMessage>>();
        messageCounter = 0L;
    }

    @Override
    public void createGroupMessage(GroupMessage pGroupMessage) {
        if (pGroupMessage != null) {
            pGroupMessage.setId(messageCounter++);
            Long groupId = pGroupMessage.getGroupId();
            List<GroupMessage> messagesByGroup = messageByGroupIdMap.get(groupId);
            if (messagesByGroup == null) {
                messagesByGroup = new ArrayList<GroupMessage>();
                messageByGroupIdMap.put(groupId, messagesByGroup);
            }
            messagesByGroup.add(pGroupMessage);
            messageByIdMap.put(pGroupMessage.getId(), pGroupMessage);
        }
    }

    @Override
    public List<GroupMessage> loadAllGroupMessages(Long pGroupId, int pStartIndex, int pEndIndex) {
        return messageByGroupIdMap.get(pGroupId);
    }

    private static final class InstanceProvider {
        private static final GroupMessageDao INSTANCE = new MockGroupMessageDaoImpl();
    }
    public static final GroupMessageDao getInstance() {
        return InstanceProvider.INSTANCE;
    }
}
