package com.g6pis.beatit.tabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.g6pis.beatit.Home;
import com.g6pis.beatit.Login;
import com.g6pis.beatit.MainActivity;
import com.g6pis.beatit.R;
 
public class ProfileTab extends Fragment implements OnClickListener {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
	
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private Home activity;
    private ProfilePictureView profilePictureView;
    private TextView username;
    
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
        View rootView = inflater.inflate(R.layout.profile_tab, container, false);
        
        ((Button)rootView.findViewById(R.id.button_cerrar_sesion)).setOnClickListener(this);

        profilePictureView = (ProfilePictureView) rootView.findViewById(R.id.imageView_profile);
        profilePictureView.setCropped(true);
        username = (TextView) rootView.findViewById(R.id.textView_username);
        
        
        username.setText(activity.username);
        profilePictureView.setProfileId(activity.userId);
        
        
        return rootView;
    }
    public void onClick(View v){
    	Session.getActiveSession().closeAndClearTokenInformation();
    	sharedPrefs = activity.getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
    	editor = sharedPrefs.edit();
    	editor.clear();
    	editor.commit();
    	Intent mainActivity = new Intent(activity.getApplicationContext(),MainActivity.class);
    	startActivity(mainActivity);
    	activity.finish();
    	
	}
    
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
                makeMeRequest(session);
        }else {
            profilePictureView.setProfileId(null);
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
    

    private void makeMeRequest(final Session session) {
        Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null) {
                        profilePictureView.setProfileId(user.getId());
                        username.setText(user.getName());
                    }
                }
                if (response.getError() != null) {
                }
            }
        }).executeAsync();


    }
    
    
    
    
    
}