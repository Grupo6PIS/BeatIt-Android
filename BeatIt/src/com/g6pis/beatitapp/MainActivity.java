package com.g6pis.beatitapp;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class MainActivity extends Activity implements OnClickListener {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	private static final int NO_CONNECTION_DIALOG = 30;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;

	private Intent home;
	private Intent login;

	private String firstName;
	private String fbId;
	private String lastName;
	private String country;
	private String userId;
	private String accessToken;
	private String imageURL;
	private boolean haveToSendScore;

	private SharedPreferences sharedPrefs;
	private Editor editor;

	private DataManager datamanager;
	private Dialog noConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		noConnection = onCreateDialog(NO_CONNECTION_DIALOG);
		noConnection.hide();

		home = new Intent(this, Home.class);
		login = new Intent(this, Login.class);

		sharedPrefs = getApplicationContext().getSharedPreferences(
				APP_SHARED_PREFS, Context.MODE_PRIVATE);
		firstName = sharedPrefs.getString("firstName", "");
		fbId = sharedPrefs.getString("fbId", "");
		lastName = sharedPrefs.getString("lastName", "");
		country = sharedPrefs.getString("country", "");
		userId = sharedPrefs.getString("userId", "");
		accessToken = sharedPrefs.getString("accessToken", "");
		imageURL = "https://graph.facebook.com/" + fbId
				+ "/picture?type=square&width=960&height=960&access_token="
				+ accessToken;
		haveToSendScore = sharedPrefs.getBoolean("haveToSendScore", false);

		Session session = Session.getActiveSession();
		if (this.isOnline()) {
			if ((fbId.isEmpty())) {
				startActivity(login);
				finish();
			} else {
				final StateDAO db = new StateDAO(this);
				final Map<String, DTState> persistedStates = db.getAllStates();
				Factory.getInstance().getIDataManager()
						.setStates(persistedStates);
				final ProgressDialog progressDialog = new ProgressDialog(
						MainActivity.this);
				progressDialog.setTitle(getResources().getString(
						R.string.loading_progress));
				progressDialog.setMessage(getResources().getString(
						R.string.please_wait));
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);
				progressDialog.show();
				Thread t = new Thread() {
					public void run() {
						userId = Factory
								.getInstance()
								.getIDataManager()
								.login(userId, fbId, firstName, lastName,
										country, imageURL, haveToSendScore);
						progressDialog.dismiss();
						startActivity(home);
						finish();
						if (persistedStates.isEmpty()) {
							Map<String, DTState> persistedStates = Factory
									.getInstance().getIDataManager()
									.getPersistedStates();
							db.addStates(persistedStates);
						}else if(Factory.getInstance().getIDataManager().isNewRound()){
							db.drop();
							StateDAO db = new StateDAO(getApplicationContext());
							Map<String, DTState> persistedStates = Factory
									.getInstance().getIDataManager()
									.getPersistedStates();
							db.addStates(persistedStates);	
						}
						Editor editor = sharedPrefs.edit();
						editor.putString("userId", userId);
						editor.putBoolean("haveToSendScore", haveToSendScore);
						editor.commit();
					}
				};
				t.start();

			}
		} else {
			noConnection.show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();

		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onDestroy() {
		noConnection.dismiss();
		super.onDestroy();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}

		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case NO_CONNECTION_DIALOG: {
			builder.setMessage(R.string.no_connection_message);
			builder.setTitle(getResources().getString(R.string.no_connection));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok, this);
		}
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	public void onClick(DialogInterface dialog, int which) {
		if (Session.getActiveSession() != null)
			Session.getActiveSession().closeAndClearTokenInformation();

		StateDAO db = new StateDAO(this);
		db.drop();

		sharedPrefs = getApplicationContext().getSharedPreferences(
				APP_SHARED_PREFS, Context.MODE_PRIVATE);
		editor = sharedPrefs.edit();
		editor.clear();
		editor.putBoolean("offline", true);
		editor.commit();
		startActivity(login);
		finish();

	}

}
