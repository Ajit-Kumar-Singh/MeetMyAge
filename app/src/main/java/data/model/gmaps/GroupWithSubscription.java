package data.model.gmaps;

import data.model.Group;

public class GroupWithSubscription {


    private Group group;

    private String subscriptionId;


    public Group getGroup ()
    {
        return group;
    }

    public void setGroup (Group group)
    {
        this.group = group;
    }

    public String getSubscriptionId ()
    {
        return subscriptionId;
    }

    public void setSubscriptionId (String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }
}
