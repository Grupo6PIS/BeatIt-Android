package com.g6pis.beatit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
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
	private boolean isUserLoggedIn;
	private boolean isResumed = false;
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

		
		  uiHelper = new UiLifecycleHelper(this, callback); 
		  uiHelper.onCreate(savedInstanceState);
		 

		this.getActionBar().hide();
		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		permissions.add("public_profile");
		permissions.add("user_location");
		permissions.add("user_hometown");
//		permission.add("user_friends");
		((LoginButton) findViewById(R.id.login_button))
				.setReadPermissions(permissions);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
		if(Session.getActiveSession().getState().isOpened()){
			((RelativeLayout) findViewById(R.id.login_relative_layout)).setVisibility(View.INVISIBLE);
		}
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
						if (session == Session.getActiveSession()) {
							if (user != null) {
								sharedPrefs =
										  getApplicationContext().getSharedPreferences(APP_SHARED_PREFS,
										  Context.MODE_PRIVATE);
								editor = sharedPrefs.edit();
								editor.putString("firstName", user.getFirstName());
								editor.putString("fbId", user.getId());
								editor.putString("lastName", user.getLastName());
								editor.putString("country", user.getLocation().getName());
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
