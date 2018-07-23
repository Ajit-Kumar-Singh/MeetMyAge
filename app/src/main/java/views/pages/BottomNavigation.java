package views.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.meetmyage.com.meetmyageapp.R;

import java.lang.reflect.Field;

public class BottomNavigation extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
EventsDetailsFragment.OnFragmentInteractionListener,
CreateGroupFragment.OnFragmentInteractionListener,
RecommendedGroupsFragment.OnFragmentInteractionListener
{
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbedlayout);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.default_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.groups);
        disableShiftMode(bottomNavigationView);
        loadDefaultFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.groups:
                                item.setCheckable(true);
                                Fragment groupsFragment = new RecommendedGroupsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, groupsFragment).commit();
                                break;
                            case R.id.addGroup:
                                item.setCheckable(true);
                                Fragment addGroupFragment = new CreateGroupFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, addGroupFragment).commit();
                                break;
                            case R.id.events:
                                item.setCheckable(true);
                                Fragment eventFragment= new EventsDetailsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, eventFragment).commit();
                                break;
                            case R.id.profile:
                                item.setCheckable(true);
                                Fragment profileFragment = new ProfileFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, profileFragment).commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    private void loadDefaultFragment()
    {
        Fragment groupsFragment = new RecommendedGroupsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, groupsFragment).commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        // Empty for now USed for interaction
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        //Empty for now used for interaction
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }
}
