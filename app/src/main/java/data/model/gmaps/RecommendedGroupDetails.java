package data.model.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import data.model.Location;

public class RecommendedGroupDetails {

    public GroupDetails getGroupDetails() {
        return groupDetails;
    }

    public void setGroupDetails(GroupDetails groupDetails) {
        this.groupDetails = groupDetails;
    }

    @SerializedName("group")
    @Expose
    private GroupDetails groupDetails;



    public GroupMembers[] getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(GroupMembers[] groupMembers) {
        this.groupMembers = groupMembers;
    }

    @SerializedName("groupMembers")
    @Expose
    public GroupMembers[] groupMembers;

}
