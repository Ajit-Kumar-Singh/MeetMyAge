package views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import data.SessionManagementUtil;
import data.model.Profile;
import views.pages.BottomNavigation;
import views.pages.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManagementUtil sessionManagementUtil = new SessionManagementUtil(this);
        Profile profile = sessionManagementUtil.getUserData();
        if (profile != null && profile.getProfileId() != null && profile.getProfileId() > 0) {
            Intent myIntent = new Intent(MainActivity.this, BottomNavigation.class);
            startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
        finish();
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}
