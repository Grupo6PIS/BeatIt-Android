package com.g6pis.beatit;

import java.util.Arrays;

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

		((LoginButton) findViewById(R.id.login_button))
				.setReadPermissions(Arrays.asList("public_profile"));



		/*
		 * final LinearLayout ll = (LinearLayout)
		 * findViewById(R.id.linear_facebook); ll.setOnClickListener(new
		 * View.OnClickListener() { public void onClick(View v) { // Perform
		 * action on click /*Intent challenge = new Intent(Login.this,
		 * Home.class); startActivity(challenge);
		 * 
		 * sharedPrefs =
		 * getApplicationContext().getSharedPreferences(APP_SHARED_PREFS,
		 * Context.MODE_PRIVATE); isUserLoggedIn =
		 * sharedPrefs.getBoolean("userLoggedInState", false); editor =
		 * sharedPrefs.edit(); editor.putBoolean("userLoggedInState", true);
		 * editor.commit();
		 * 
		 * Intent signupSuccessHome = new Intent(Login.this, Home.class);
		 * signupSuccessHome.putExtra("reqFrom", "login");
		 * startActivity(signupSuccessHome); finish();
		 * 
		 * 
		 * } });
		 */
		// isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
		// if (isUserLoggedIn) {
		// Intent intent = new Intent(this, Home.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		// finish();
		// }
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.login, menu); return true; }
	 */

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

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
			
		} else if (state.isClosed()) {
		}
		// finish();
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
								editor.putString("username", user.getName());
								editor.putString("userId", user.getId());
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
