package com.g6pis.beatit;


import com.g6pis.beatit.tabs.ChallengesMenuTab;
import com.g6pis.beatit.tabs.ProfileTab;
import com.g6pis.beatit.tabs.RankingTab;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Home extends FragmentActivity {
	
	// Declare Tab Variable
		public static Context appContext;
	    ActionBar.Tab Tab1, Tab2, Tab3;
//	    Fragment challengesMenuTab = new ChallengesMenuTab();
//	    Fragment rankingTab = new RankingTab();
//	    Fragment profileTab = new ProfileTab();
	    
	     private static final String APP_SHARED_PREFS = "asdasd_preferences";
	     SharedPreferences sharedPrefs;
	     Editor editor;
	     private boolean isUserLoggedIn;
	   
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tabs);
	               
	        sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
	        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
	        if (!isUserLoggedIn) {
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            finish();
	        }
	        
	        
	        
	        ActionBar actionBar = getActionBar();
	        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
	        actionBar.setCustomView(R.layout.action_bar);
	        actionBar.setDisplayHomeAsUpEnabled(false);
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        setTitle(R.string.app_name);
	        
	        ActionBar.Tab tab1 = actionBar.newTab().setText(R.string.challenges_tab);
	        ActionBar.Tab tab2 = actionBar.newTab().setText(R.string.ranking_tab);
	        ActionBar.Tab tab3 = actionBar.newTab().setText(R.string.profile_tab);
	        
	        Fragment challengesMenuTab = new ChallengesMenuTab();
	        Fragment rankingTab = new RankingTab();
	        Fragment profileTab = new ProfileTab();
	        
	       
	        
	        tab1.setTabListener(new MyTabsListener(challengesMenuTab));
	        tab2.setTabListener(new MyTabsListener(rankingTab));
	        tab3.setTabListener(new MyTabsListener(profileTab));

	        actionBar.addTab(tab1);
	        actionBar.addTab(tab2);
	        actionBar.addTab(tab3);
	    }
	    
	    class MyTabsListener implements ActionBar.TabListener {
	    	public Fragment fragment;

	    	public MyTabsListener(Fragment fragment) {
	    	    this.fragment = fragment;
	    	}

	    	@Override
	    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    	    //do what you want when tab is reselected, I do nothing
	    	}

	    	@Override
	    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    	    ft.replace(R.id.fragment_placeholder, fragment);
	    	}

	    	@Override
	    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    	    ft.remove(fragment);
	    	}
	    	}

	    
	    @Override
	    protected void onResume() {
	        sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
	        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
	        if (!isUserLoggedIn) {
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            finish();
	        }
	        super.onResume();
	    }

	    @Override
	    protected void onRestart() {
	        sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
	        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
	        if (!isUserLoggedIn) {
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            finish();
	        }
	        super.onRestart();
	    }
}
