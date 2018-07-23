package data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import data.model.Group;
import data.model.Location;
import data.model.Profile;

public class SessionManagementUtil {
    // Shared Preferences
    public static SharedPreferences pref;

    // Editor for Shared preferences
    public static Editor editor;

    // Context
    public static Context _context;

    // Shared pref mode
    static int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MeetMyAge";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_WORK ="work";

    public static final String KEY_ABOUT = "aboutme";

    public  static final String KEY_LOCATION = "location";

    public  static final String KEY_RECOMMENDED_GROUPS = "recommended_groups";

    public  static final String KEY_PROFILE_PATH = "profilepicture";

    public  static final String KEY_SELECTED_GROUP = "selectedGroup";

    public static Gson gson = new GsonBuilder().serializeNulls().create();

    // Constructor
    public SessionManagementUtil(Context context){
        if (_context == null)
        {
            _context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }
    }

    public static void saveRecommendedGroups(List<Group> pGroups) {
        editor.putString(KEY_RECOMMENDED_GROUPS,gson.toJson(pGroups));
        Log.d("KEY_RECOMMENDED_GROUPS", gson.toJson(pGroups));
        editor.commit();
    }

    /**
     * Create login session
     * */
    public static void createLoginSession(int id, String name, String email, String about, String work){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_ABOUT,about);
        editor.putString(KEY_WORK,work);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public static void updateProfile(String name, String email, String about, String work){
        // Storing login value as TRUE

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ABOUT,about);
        editor.putString(KEY_WORK,work);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    public static void setSelectedGroup(Group pGroup) {
        Log.d("KEY_SELECTED_GROUP", gson.toJson(pGroup));
        editor.putString(KEY_SELECTED_GROUP,gson.toJson(pGroup));
        editor.commit();
    }


    public static Group getSelectedGroup() {
        Type fooType = new TypeToken<Group>() {}.getType();
        Group grp = gson.fromJson(pref.getString(KEY_SELECTED_GROUP,""), fooType);
        return grp;
    }
    public static void updateLocation(Location loc)
    {
        editor.putString(KEY_LOCATION,gson.toJson(loc));
        Log.d("LOCATION", gson.toJson(loc));
        editor.commit();
    }

    public static Location getLocation()
    {
        Location loc = gson.fromJson(pref.getString(KEY_LOCATION,""), Location.class);
        return loc;
    }

    public static void saveProfileImagePath(String path)
    {
        editor.putString(KEY_PROFILE_PATH, path);
        editor.commit();
    }

    public static String getImagePath()
    {
        return pref.getString(KEY_PROFILE_PATH,"");
    }

     public static Profile getUserData()
     {
         return  new Profile(pref.getInt(KEY_ID,0), pref.getString(KEY_NAME,""), pref.getString(KEY_ABOUT,""),pref.getString(KEY_WORK,""), getLocation());
     }

    public static List<Group> getRecommendedGroups() {
        Type fooType = new TypeToken<List<Group>>() {}.getType();
        List<Group>loc = gson.fromJson(pref.getString(KEY_RECOMMENDED_GROUPS,""), fooType);
        return loc;
    }

    public void clearAllData(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public static boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}