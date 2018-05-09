package util;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import model.Profile;

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

    // Constructor
    public SessionManagementUtil(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public static void createLoginSession(int id, String name, String email, String work, String about){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putInt(KEY_ID,id);
        editor.putString(KEY_WORK,work);
        editor.putString(KEY_ABOUT,about);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }
     public static Profile getUserData()
     {
         return new Profile(pref.getInt(KEY_ID,0), pref.getString(KEY_NAME,""), pref.getString(KEY_ABOUT,""),pref.getString(KEY_WORK,""));
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
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}