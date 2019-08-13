package mma.services.impl;

import java.util.List;

import data.model.Group;
import mma.dao.factory.DaoFactory;
import mma.services.GroupMessageService;
import mma.services.GroupService;

public class GroupServiceImpl implements GroupService {
    private GroupServiceImpl(){}
    public static GroupServiceImpl getInstance() {
        return GroupServiceImpl.InstanceProvider.INSTANCE;
    }
    private static final  class InstanceProvider {
        private static final GroupServiceImpl INSTANCE = new GroupServiceImpl();
    }
    @Override
    public Group findGroupById(long pGroupId) {
        return DaoFactory.getInstance().getGroupDao().findGroupById(pGroupId);
    }

    @Override
    public List<Group> findJoinedGroups(long pProfileId) {
        return DaoFactory.getInstance().getGroupDao().findJoinedGroups(pProfileId);
    }
}
