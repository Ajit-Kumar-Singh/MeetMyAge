package mma.dao;

import java.util.List;

import data.model.Group;

public interface GroupDao {
    Group findGroupById(long pGroupId);

    List<Group> findJoinedGroups(long pProfileId);
}
