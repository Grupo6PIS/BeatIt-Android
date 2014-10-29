package com.g6pis.beatitapp;

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
import android.support.v4.app.FragmentActivity;
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

public class Home extends FragmentActivity implements OnClickListener {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setTitle(R.string.app_name);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
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
		this.challengeDialog = onCreateDialog(CHALLENGE_DIALOG);
		this.challengeDialog.hide();
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

			Factory.getInstance().getIDataManager().logout();
			sharedPrefs = getApplicationContext().getSharedPreferences(
					APP_SHARED_PREFS, Context.MODE_PRIVATE);
			editor = sharedPrefs.edit();
			editor.clear();
			editor.commit();
			Intent mainActivity = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(mainActivity);
			this.finish();
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

}
