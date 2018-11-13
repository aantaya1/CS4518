package com.aantaya.imagewars;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aantaya.imagewars.Fragments.LeaderboardFragment;
import com.aantaya.imagewars.Fragments.UsersImagesFragment;
import com.aantaya.imagewars.Fragments.VoteFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0)
            return VoteFragment.newInstance();
        else if (position == 1)
            return LeaderboardFragment.newInstance();
        else
            return UsersImagesFragment.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}
