package com.g6pis.beatitapp.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Challenge Menu fragment activity
            return new ChallengesMenuTab();
        case 1:
            //Ranking fragment activity
            return new RankingTab();
        case 2:
            // Movies fragment activity
            return new ProfileTab();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}

