package com.g6pis.beatitapp;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.persistence.StateDAO;

public class MainActivity extends Activity {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	}; 

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
		this.getActionBar().hide();
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

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
		boolean isClosed = session.getState().isClosed();
		if (fbId.isEmpty()) {
			startActivity(login);
			finish();
		} else {
			StateDAO db = new StateDAO(this);
			Map<String,DTState> persistedStates = db.getAllStates();
			DataManager.getInstance().setStates(persistedStates);
			userId = DataManager.getInstance().login(userId,fbId, firstName, lastName, country, imageURL);
			if(persistedStates.isEmpty()){
				persistedStates = DataManager.getInstance().getPersistedStates();
				db.addStates(persistedStates);
			}
			Editor editor = sharedPrefs.edit();
			editor.putString("userId", userId);
			editor.commit();
			startActivity(home);
			finish();
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (isResumed) {

			// check for the OPENED state instead of session.isOpened() since
			// for the
			// OPENED_TOKEN_UPDATED state, the selection fragment should already
			// be showing.
			if (state.equals(SessionState.OPENED)) {
				userId = DataManager.getInstance().login(userId,fbId, firstName, lastName, country, imageURL);
				Editor editor = sharedPrefs.edit();
				editor.putString("userId", userId);
				editor.commit();
				startActivity(home);
			} else if (state.isClosed()) {
				startActivity(login);
			}
			finish();
		}
	}
	
	
	private void makeMeRequest(final Session session) {
        Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null) {
                    	firstName = user.getFirstName();
                    	lastName = user.getLastName();
                        fbId =  user.getId();
                        country = user.getLocation().getName();
                    }
                }
                if (response.getError() != null) {
                }
            }
        }).executeAsync();


    }
}
