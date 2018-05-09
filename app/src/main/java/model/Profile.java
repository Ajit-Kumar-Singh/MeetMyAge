package model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Profile {

    private int profileId;
    @SerializedName("profileName")
    private String profileName;
    @SerializedName("profileStory")
    private String profileStory;
    @SerializedName("profileWork")
    private String profileWork;

    public Profile(int profileId, String profileName, String profileStory, String profileWork) {
        this.profileId = profileId;
        this.profileName = profileName;
        this.profileStory = profileStory;
        this.profileWork = profileWork;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
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


}