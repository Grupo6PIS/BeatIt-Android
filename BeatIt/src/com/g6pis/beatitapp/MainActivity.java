package com.g6pis.beatitapp;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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

public class MainActivity extends Activity {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
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
	
	private SharedPreferences sharedPrefs;
	private Editor editor;
	
	private DataManager datamanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		

		home = new Intent(this, Home.class);
		login = new Intent(this, Login.class);

		
		sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", "");
        fbId = sharedPrefs.getString("fbId", "");
        lastName = sharedPrefs.getString("lastName", "");
        country = sharedPrefs.getString("country", "");
        userId = sharedPrefs.getString("userId", ""); 
        accessToken = sharedPrefs.getString("accessToken", "");
		imageURL = "https://graph.facebook.com/"+fbId+"/picture?type=square&width=960&height=960&access_token="+accessToken;
        
		Session session = Session.getActiveSession();
		if(this.isOnline()){
			if ((fbId.isEmpty())) {
				startActivity(login);
				finish();
			} else {
				StateDAO db = new StateDAO(this);
				Map<String,DTState> persistedStates = db.getAllStates();
				Factory.getInstance().getIDataManager().setStates(persistedStates);
				final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setTitle(getResources().getString(R.string.loading_progress));
				progressDialog.setMessage(getResources().getString(R.string.please_wait));
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);
				progressDialog.show();
				Thread t = new Thread(){
				    public void run(){
				    	userId = Factory.getInstance().getIDataManager().login(userId,fbId, firstName, lastName, country, imageURL);
				    	progressDialog.dismiss();
				    	startActivity(home);
				    	finish();
				    }
				};
				t.start();
				if(persistedStates.isEmpty()){
					persistedStates = Factory.getInstance().getIDataManager().getPersistedStates();
					db.addStates(persistedStates);
				}
				Editor editor = sharedPrefs.edit();
				editor.putString("userId", userId);
				editor.commit();
			}
		}else{
			Session.getActiveSession().closeAndClearTokenInformation();

			StateDAO db = new StateDAO(this);
			db.drop();
			
			sharedPrefs = getApplicationContext()
					.getSharedPreferences(APP_SHARED_PREFS,
							Context.MODE_PRIVATE);
			editor = sharedPrefs.edit();
			editor.clear();
			editor.commit();
			startActivity(login);
			finish();
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
		super.onDestroy();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);


	}

	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
