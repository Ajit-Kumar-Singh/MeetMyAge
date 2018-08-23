package views.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.meetmyage.com.meetmyageapp.R;

import java.lang.reflect.Field;

public class BottomNavigation extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
EventsDetailsFragment.OnFragmentInteractionListener,
CreateGroupFragment.OnFragmentInteractionListener,
RecommendedGroupsFragment.OnFragmentInteractionListener,
ProfileDetails.OnFragmentInteractionListener,
ProfileSettings.OnFragmentInteractionListener,
JoinedGroupMembers.OnListFragmentInteractionListener
{
    private Bitmap mBitmap = null;
    private BottomNavigationView bottomNavigationView;
    private boolean mKeyboardVisible = false;
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
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, groupsFragment).addToBackStack("rootStack").commit();
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
    @Override
    protected void onResume() {
        super.onResume();
        getBottomNavigationView().getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPause() {
        super.onPause();
        getBottomNavigationView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutKeyboardVisibilityListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final Rect rectangle = new Rect();
                    View contentView = getBottomNavigationView();
                    contentView.getWindowVisibleDisplayFrame(rectangle);
                    int screenHeight = contentView.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // If keypad is shown, the rectangle.bottom is smaller than that before.
                    int keypadHeight = screenHeight - rectangle.bottom;
                    // 0.15 ratio is perhaps enough to determine keypad height.
                    boolean isKeyboardNowVisible = keypadHeight > screenHeight * 0.15;

                    if (mKeyboardVisible != isKeyboardNowVisible) {
                        if (isKeyboardNowVisible) {
                            onKeyboardShown();
                        } else {
                            onKeyboardHidden();
                        }
                    }

                    mKeyboardVisible = isKeyboardNowVisible;
                    }
                };


    private void onKeyboardShown() {
        getBottomNavigationView().setVisibility(View.GONE);
    }

    private void onKeyboardHidden() {
        getBottomNavigationView().setVisibility(View.VISIBLE);
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


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
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

    @Override
    public void onListFragmentInteraction(JoinedGroupMemberDetail.DummyItem item) {

    }
}
