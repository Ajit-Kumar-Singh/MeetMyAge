package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("profileId")
    @Expose
    private Integer profileId;
    @SerializedName("profileName")
    @Expose
    private String profileName;
    @SerializedName("profileStory")
    @Expose
    private String profileStory;
    @SerializedName("profileWork")
    @Expose
    private String profileWork;
    @SerializedName("profileEmail")
    @Expose
    private String profileEmail;
    @SerializedName("location")
    @Expose
    private Location location;

    public Profile(int id, String name, String about, String work, Location loc) {
        this.profileId = id;
        this.profileName = name;
        this.profileStory = about;
        this.profileWork = work;
        this.location = loc;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileStory() {
        return profileStory;
    }

    public void setProfileStory(String profileStory) {
        this.profileStory = profileStory;
    }

    public String getProfileWork() {
        return profileWork;
    }

    public void setProfileWork(String profileWork) {
        this.profileWork = profileWork;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
