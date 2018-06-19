package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupRequest {

    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupStory")
    @Expose
    private String groupStory;
    @SerializedName("location")
    @Expose
    private Location location;

    /**
     * No args constructor for use in serialization
     *
     */
    public GroupRequest() {
    }

    /**
     *
     * @param groupName
     * @param location
     * @param groupStory
     */
    public GroupRequest(String groupName, String groupStory, Location location) {
        super();
        this.groupName = groupName;
        this.groupStory = groupStory;
        this.location = location;
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