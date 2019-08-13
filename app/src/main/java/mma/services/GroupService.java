package mma.services;

import java.util.List;

import data.model.Group;

public interface GroupService {
    Group findGroupById(long pGroupId);

    List<Group> findJoinedGroups(long pProfileId);
}
