package com.g6pis.beatit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.g6pis.beatit.challenges.usainbolt.UsainBoltUI;

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

	Intent home;
	Intent login;
	
	String username;
	String userId;
	 SharedPreferences sharedPrefs;
     Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getActionBar().hide();

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		home = new Intent(this, Home.class);
		login = new Intent(this, Login.class);

		setContentView(R.layout.activity_main);
		/*if(getIntent().getExtras() != null){
			username = getIntent().getExtras().getString("username");
			userId = getIntent().getExtras().getString("userId");
		}*/


		
		/*Session.getActiveSession().addCallback(new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										username = user.getUsername();
										userId = user.getId();

									}
								}
							}).executeAsync();

				}

			}
		});*/
		sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPrefs.getString("username", "");
        userId = sharedPrefs.getString("userId", "");
		
		// TODO comprobar si está logueado.
		Session session = Session.getActiveSession();
		boolean isClosed = session.getState().isClosed();
		if (username.isEmpty()) {
			startActivity(login);
			finish();
		} else {
			home.putExtra("username", username);
            home.putExtra("userId", userId);
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
				makeMeRequest(session);
				home.putExtra("username", username);
	            home.putExtra("userId", userId);
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
                    	username = user.getUsername();
                        userId =  user.getId();
                    }
                }
                if (response.getError() != null) {
                }
            }
        }).executeAsync();


    }
}
