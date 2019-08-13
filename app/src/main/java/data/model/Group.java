package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {
    @SerializedName("groupId")
    @Expose
    private Integer groupId;
    @SerializedName("groupName")
    @Expose
    private String groupName;

    @SerializedName("groupImageUrl")
    @Expose
    private String groupImageUrl;

    @SerializedName("groupStory")
    @Expose
    private String groupStory;
    @SerializedName("location")
    @Expose
    private Location location;

    private List<Profile> members;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupStory() {
        return groupStory;
    }

    public void setGroupStory(String groupStory) {
        this.groupStory = groupStory;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setGroupImageUrl(String pGroupImageUrl) {
        this.groupImageUrl = pGroupImageUrl;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }

    public List<Profile> getMembers() {return members;};

}
