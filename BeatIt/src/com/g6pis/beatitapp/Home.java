package com.g6pis.beatitapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.facebook.Session;
import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGameUI;
import com.g6pis.beatitapp.challenges.catchme.CatchMeUI;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;
import com.g6pis.beatitapp.tabs.ChallengesMenuTab;
import com.g6pis.beatitapp.tabs.ProfileTab;
import com.g6pis.beatitapp.tabs.RankingTab;
import com.g6pis.beatitapp.tabs.TabsPagerAdapter;

public class Home extends FragmentActivity implements OnClickListener, ActionBar.TabListener, OnPageChangeListener {
	private static final int CONFIRMATION_DIALOG = 60;
	private static final int CHALLENGE_DIALOG = 50;
	// Declare Tab Variable
	public static Context appContext;
	ActionBar.Tab Tab1, Tab2, Tab3;

	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	SharedPreferences sharedPrefs;
	Editor editor;
	private boolean isUserLoggedIn;
	public ImageButton refreshButton;
	public ImageButton retryButton;
	public Dialog confirmationDialog;
	public Dialog challengeDialog;
	public int challenge = 0;
	
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setTitle(R.string.app_name);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		retryButton = (ImageButton) findViewById(R.id.retry_button);
		retryButton.setVisibility(View.INVISIBLE);
    	refreshButton.setVisibility(View.INVISIBLE);

		
		
		
		viewPager = (ViewPager) findViewById(R.id.fragment_placeholder);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(this);
	
		actionBar.addTab(actionBar.newTab().setText(R.string.challenges_tab)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.ranking_tab)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.profile_tab)
				.setTabListener(this));
		

		this.confirmationDialog = onCreateDialog(CONFIRMATION_DIALOG);
		this.confirmationDialog.hide();
		this.challengeDialog = onCreateDialog(CHALLENGE_DIALOG);
		this.challengeDialog.hide();
	}

	
	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		confirmationDialog.dismiss();
	}

	public void goToProfileFragment() {
		getActionBar().selectTab(getActionBar().getTabAt(2));

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case CONFIRMATION_DIALOG: {
			builder.setMessage(R.string.logout_confirmation);
			builder.setTitle(getResources().getString(
					R.string.logout_confirmation_title));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok, this);
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}
		case CHALLENGE_DIALOG: {
			builder.setMessage(R.string.challenge_under_construction);
			builder.setTitle(getResources().getString(
					R.string.challenge_under_construction_title));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_button, this);
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}

		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (challenge) {
		case 0: {
			Session.getActiveSession().closeAndClearTokenInformation();

			StateDAO db = new StateDAO(this);
			db.drop();
			final ProgressDialog progressDialog = new ProgressDialog(Home.this);
			progressDialog.setTitle(getResources().getString(R.string.saving_progress));
			progressDialog.setMessage(getResources().getString(R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
			Thread t = new Thread(){
			    public void run(){
			    	Factory.getInstance().getIDataManager().logout();
					Intent mainActivity = new Intent(getApplicationContext(),
							MainActivity.class);
					progressDialog.dismiss();
					startActivity(mainActivity);
					finish();
			    }
			};
			t.start();
			
			sharedPrefs = getApplicationContext().getSharedPreferences(
					APP_SHARED_PREFS, Context.MODE_PRIVATE);
			editor = sharedPrefs.edit();
			editor.clear();
			editor.commit();

		}
			break;
		case 5: {
			Intent intent = new Intent(this, BouncingGameUI.class);
			startActivity(intent);
			this.finish();
		}
			break;
		case 7: {
			Intent intent = new Intent(this, CatchMeUI.class);
			startActivity(intent);
			this.finish();
		}
			break;
		}
	}
	
	
	@Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onPageSelected(int position) {
        // on changing the page
        // make respected tab selected
        actionBar.setSelectedNavigationItem(position);
        if(position==1)
        	refreshButton.setVisibility(View.VISIBLE);
        else
        	refreshButton.setVisibility(View.INVISIBLE);
        	
    }
 
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
 
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

}
