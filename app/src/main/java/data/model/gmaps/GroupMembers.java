package data.model.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import data.model.Group;
import data.model.Profile;

public class GroupMembers {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("groupId")
        @Expose
        private Integer groupId;
        @SerializedName("profileId")
        @Expose
        private Integer profileId;
        @SerializedName("group")
        @Expose
        private Group group;
        @SerializedName("subscriptionDate")
        @Expose
        private Date subscriptionDate;
        @SerializedName("profile")
        @Expose
        private Profile profile;

        public Integer getId ()
        {
            return id;
        }

        public void setId (Integer id)
        {
            this.id = id;
        }

        public Integer getGroupId ()
        {
            return groupId;
        }

        public void setGroupId (Integer groupId)
        {
            this.groupId = groupId;
        }

        public Integer getProfileId ()
        {
            return profileId;
        }

        public void setProfileId (Integer profileId)
        {
            this.profileId = profileId;
        }

        public Group getGroup ()
    {
        return group;
    }

        public void setGroup (Group group)
        {
            this.group = group;
        }

        public Date getSubscriptionDate ()
    {
        return subscriptionDate;
    }

        public void setSubscriptionDate (Date subscriptionDate)
        {
            this.subscriptionDate = subscriptionDate;
        }

        public Profile getProfile ()
        {
            return profile;
        }

        public void setProfile (Profile profile)
        {
            this.profile = profile;
        }

}
