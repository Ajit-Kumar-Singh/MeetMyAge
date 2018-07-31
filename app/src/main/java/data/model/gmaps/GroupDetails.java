package data.model.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import data.model.Location;

public class GroupDetails {

    @SerializedName("groupId")
    @Expose
    private Integer groupId;

    public Integer getGroupId() { return this.groupId; }

    public void setGroupId(Integer groupId) { this.groupId = groupId; }
    @SerializedName("groupName")
    @Expose
    private String groupName;

    public String getGroupName() { return this.groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }
    @SerializedName("groupStory")
    @Expose
    private String groupStory;

    public String getGroupStory() { return this.groupStory; }

    public void setGroupStory(String groupStory) { this.groupStory = groupStory; }
    @SerializedName("groupImageId")
    @Expose
    private Integer groupImageId;

    public Integer getGroupImageId() { return this.groupImageId; }

    public void setGroupImageId(Integer groupImageId) { this.groupImageId = groupImageId; }
    @SerializedName("locationId")
    @Expose
    private Integer locationId;

    public Integer getLocationId() { return this.locationId; }

    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() { return this.location; }

    public void setLocation(Location location) { this.location = location; }
    @SerializedName("groupImage")
    @Expose
    private Object groupImage;

    public Object getGroupImage() { return this.groupImage; }

    public void setGroupImage(Object groupImage) { this.groupImage = groupImage; }

}
