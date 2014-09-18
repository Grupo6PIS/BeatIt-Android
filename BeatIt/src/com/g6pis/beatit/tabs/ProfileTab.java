package com.g6pis.beatit.tabs;

import com.g6pis.beatit.ChallengeFinished;
import com.g6pis.beatit.Login;
import com.g6pis.beatit.R;

import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.app.Fragment;
import android.content.Intent;
 
public class ProfileTab extends Fragment implements OnClickListener {

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_tab, container, false);
        
        ((Button)rootView.findViewById(R.id.button_cerrar_sesion)).setOnClickListener(this);
        
        return rootView;
    }
    public void onClick(View v){
    	Intent login = new Intent(getActivity().getApplicationContext(), Login.class);
    	startActivity(login);
    	this.getActivity().finish();
	}
}