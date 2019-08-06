package mma.dao.impl.mock;

import java.util.List;
import java.util.Map;

import data.MockData;
import data.model.Group;
import data.model.message.GroupMessage;
import mma.dao.GroupDao;
import mma.dao.GroupMessageDao;

public class MockGroupDaoImpl implements GroupDao {
    private MockGroupDaoImpl() {
        //private constructure
    }
    public static final GroupDao getInstance() {
        return InstanceProvider.INSTANCE;
    }
    private static final class InstanceProvider {
        private static final GroupDao INSTANCE = new MockGroupDaoImpl();
    }

    @Override
    public Group findGroupById(long pGroupId) {
        return MockData.groups.get(pGroupId);
    }

    @Override
    public List<Group> findJoinedGroups(long pProfileId) {
        return MockData.my_groups.get(1L);
    }
}
