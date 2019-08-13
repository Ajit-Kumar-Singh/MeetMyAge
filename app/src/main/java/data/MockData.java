package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.model.Group;
import data.model.Location;
import data.model.Profile;

public class MockData {
    public static final Map<Long, List<Group>> my_groups = new HashMap<Long, List<Group>>();
    public static final Map<Long, Group> groups = new HashMap<Long, Group>();
    static {

        Profile profile1 = new Profile(1, "Omprakash Ola", null, null, null);
        profile1.setProfileEmail("omprakash_fxxeucb_ola@tfbnw.net");

        Profile profile2 = new Profile(2, "Ajit Singh", null, null, null);
        profile2.setProfileEmail("ajit_onelibg_singh@tfbnw.net");

        Group olaGroup1 = new Group();
        olaGroup1.setGroupId(1);
        olaGroup1.setGroupName("Hyderabad Ke Potte Potia");
        olaGroup1.setGroupStory("Your limitation—it’s only your imagination");
        olaGroup1.setGroupImageUrl("@drawable/group_sample_image_2");
        List<Profile> members = new ArrayList<Profile>();
        members.add(profile1);
        members.add(profile2);
        olaGroup1.setMembers(members);

        Group olaGroup2 = new Group();
        olaGroup2.setGroupId(2);
        olaGroup2.setGroupName("Vasudhev Kutumbkam");
        olaGroup2.setGroupStory("Rasal's Family Members");
        olaGroup2.setGroupImageUrl("@drawable/group_sample_image_2");
        olaGroup2.setMembers(members);

        Group olaGroup3 = new Group();
        olaGroup3.setGroupId(3);
        olaGroup3.setGroupName("Friends Forever");
        olaGroup3.setGroupStory("Ye dosti hum nahi todenge, todenge dum magar tera sath na chodenge");
        olaGroup3.setGroupImageUrl("@drawable/group_sample_image_3");
        olaGroup3.setMembers(members);
        groups.put(1L, olaGroup1);
        groups.put(2L, olaGroup2);
        groups.put(3L, olaGroup3);

        List<Group> olaGroups = new ArrayList<Group>();
        olaGroups.add(olaGroup1);
        olaGroups.add(olaGroup2);
        olaGroups.add(olaGroup3);
        my_groups.put(1L, olaGroups);
    }

}
