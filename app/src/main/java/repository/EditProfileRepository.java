package repository;

import data.SessionManagementUtil;
import data.model.Profile;

public class EditProfileRepository
{
    private static EditProfileRepository sInstance;
    public static EditProfileRepository getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new EditProfileRepository();
        }
        return sInstance;
    }

    public static Profile getSavedProfileData()
    {
        return SessionManagementUtil.getUserData();
    }
}
