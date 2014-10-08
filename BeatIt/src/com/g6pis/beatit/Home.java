package com.g6pis.beatit;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.Session;
import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.persistence.StateDAO;
import com.g6pis.beatit.tabs.ChallengesMenuTab;
import com.g6pis.beatit.tabs.ProfileTab;
import com.g6pis.beatit.tabs.RankingTab;

public class Home extends FragmentActivity implements OnClickListener {
	private static final int CONFIRMATION_DIALOG = 60;
	// Declare Tab Variable
	public static Context appContext;
	ActionBar.Tab Tab1, Tab2, Tab3;

	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	SharedPreferences sharedPrefs;
	Editor editor;
	private boolean isUserLoggedIn;
	public DataManager datamanager;
	public ImageButton refreshButton;
	public ImageButton retryButton;
	public Dialog confirmationDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setTitle(R.string.app_name);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		retryButton = (ImageButton) findViewById(R.id.retry_button);
		retryButton.setVisibility(View.INVISIBLE);

		ActionBar.Tab tab1 = actionBar.newTab()
				.setText(R.string.challenges_tab);
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
		
		this.confirmationDialog = onCreateDialog(CONFIRMATION_DIALOG);
		this.confirmationDialog.hide();
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// do what you want when tab is reselected, I do nothing
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
			builder.setTitle(getResources().getString(R.string.logout_confirmation_title));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok, this);
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
			Session.getActiveSession().closeAndClearTokenInformation();

			StateDAO db = new StateDAO(this);
			db.drop();

			datamanager.getInstance().logout();
			sharedPrefs = getApplicationContext()
					.getSharedPreferences(APP_SHARED_PREFS,
							Context.MODE_PRIVATE);
			editor = sharedPrefs.edit();
			editor.clear();
			editor.commit();
			Intent mainActivity = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(mainActivity);
			this.finish();
		}

}
