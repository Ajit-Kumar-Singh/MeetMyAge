package com.meetmyage.com.meetmyageapp.pages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new GroupDetailsFragment();
        } else if (position == 1) {
            return new CreateGroupFragment();
        } else if (position == 2) {
            return new EventsDetailsFragment();
        } else {
            return new ProfileFragmentHolder();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }
}