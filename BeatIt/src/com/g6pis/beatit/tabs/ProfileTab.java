package com.g6pis.beatit.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.g6pis.beatit.Home;
import com.g6pis.beatit.MainActivity;
import com.g6pis.beatit.R;
import com.g6pis.beatit.connection.ImageLoadTask;
import com.g6pis.beatit.persistence.StateDAO;

public class ProfileTab extends Fragment implements OnClickListener {
	private static final int CONFIRMATION_DIALOG = 60;
	private static final String APP_SHARED_PREFS = "asdasd_preferences";

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private Home activity;
	private ImageView profilePicture;
	private TextView username;
	private TextView country;

	SharedPreferences sharedPrefs;
	Editor editor;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (Home) getActivity();
		uiHelper = new UiLifecycleHelper(getActivity(), sessionCallback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.profile_tab, container, false);

		((Button) rootView.findViewById(R.id.logout_button))
				.setOnClickListener(this);

		profilePicture = (ImageView) rootView
				.findViewById(R.id.imageView_profile);
		username = (TextView) rootView.findViewById(R.id.textView_username_ranking);
		country = (TextView) rootView.findViewById(R.id.textView_user_country);

		username.setText(activity.datamanager.getInstance().getUser()
				.getFirstName()
				+ " "
				+ activity.datamanager.getInstance().getUser().getLastName());

		country.setText(activity.datamanager.getInstance().getUser()
				.getCountry());

		new ImageLoadTask(activity.datamanager.getInstance().getUser()
				.getImageURL(), profilePicture).execute(null, null);

		((Home) getActivity()).refreshButton.setVisibility(View.INVISIBLE);
		
		
		return rootView;
	}

	public void onClick(View v) {
		
		activity.confirmationDialog.show();
	}

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			// makeMeRequest(session);
		} else {

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
		activity = null;
	}


}