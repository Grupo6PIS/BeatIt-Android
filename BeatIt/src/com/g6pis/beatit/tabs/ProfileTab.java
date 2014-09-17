package com.g6pis.beatit.tabs;

import com.g6pis.beatit.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
 
public class ProfileTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_tab, container, false);
        return rootView;
    }
 
}