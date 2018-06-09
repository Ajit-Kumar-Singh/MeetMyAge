package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("groupId")
    @Expose
    private Integer groupId;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupStory")
    @Expose
    private String groupStory;
    @SerializedName("location")
    @Expose
    private Location location;


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


}
