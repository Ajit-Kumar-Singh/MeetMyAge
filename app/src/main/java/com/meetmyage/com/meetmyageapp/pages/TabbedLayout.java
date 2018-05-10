package com.meetmyage.com.meetmyageapp.pages;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.meetmyage.com.meetmyageapp.R;

public class TabbedLayout extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
EventsDetailsFragment.OnFragmentInteractionListener,
CreateGroupFragment.OnFragmentInteractionListener,
GroupDetailsFragment.OnFragmentInteractionListener,
ProfileFragmentHolder.OnFragmentInteractionListener
{
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbedlayout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setIcon(R.drawable.ic_group_black_24dp);
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setIcon(R.drawable.ic_add_black_24dp);
        tabLayout.addTab(secondTab);

        TabLayout.Tab thirdtab = tabLayout.newTab();
        thirdtab.setIcon(R.drawable.ic_whatshot_black_24dp);
        tabLayout.addTab(thirdtab);

        TabLayout.Tab fourthTab = tabLayout.newTab();
        fourthTab.setIcon(R.drawable.ic_person_outline_black_24dp);
        tabLayout.addTab(fourthTab);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Empty for now USed for interaction
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        //Empty for now used for interaction
    }
}
