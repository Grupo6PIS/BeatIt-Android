package com.g6pis.beatitapp;


import java.util.Arrays;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class Login extends Activity {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	SharedPreferences sharedPrefs;
	Editor editor;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	String username;
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		
		  uiHelper = new UiLifecycleHelper(this, callback); 
		  uiHelper.onCreate(savedInstanceState);
		
		
		LoginButton loginButton = ((LoginButton) findViewById(R.id.login_button));
		loginButton.setReadPermissions(Arrays.asList("email", "user_location"));
	
		
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();

		if(Session.getActiveSession().getState().isOpened()){
			((RelativeLayout) findViewById(R.id.login_relative_layout)).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
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
		if (state.equals(SessionState.OPENED)) {
			makeMeRequest(session);	
		}
	}

	private void makeMeRequest(final Session session) {
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if((session.isOpened()) && (session == Session.getActiveSession())) {
							if (user != null) {
								sharedPrefs =
										  getApplicationContext().getSharedPreferences(APP_SHARED_PREFS,
										  Context.MODE_PRIVATE);
								editor = sharedPrefs.edit();
								editor.putString("firstName", user.getFirstName());
								editor.putString("fbId", user.getId());
								editor.putString("lastName", user.getLastName());
								editor.putString("accessToken", session.getAccessToken());
								if(user.getLocation() != null){
									editor.putString("country",user.getLocation().getName());									
								} else {
									editor.putString("country","");
								}
								editor.commit();
								Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
								startActivity(mainActivity);
								finish();
							}
						}
						if (response.getError() != null) {
						}
					}
				});
		request.executeAsync();

	}

}
