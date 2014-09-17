package com.g6pis.beatit.tabs;


 
import com.g6pis.beatit.R;
import com.g6pis.beatit.R.id;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar;
 
public class TabListener implements ActionBar.TabListener {
 
    Fragment fragment;
 
    public TabListener(Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.fragment = fragment;
    }
 

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		//ft.replace(R.id., fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}