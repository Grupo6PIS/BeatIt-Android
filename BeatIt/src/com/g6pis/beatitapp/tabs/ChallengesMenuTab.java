package com.g6pis.beatitapp.tabs;

import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDogFinished;
import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDogUI;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlayFinished;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlayUI;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBoltFinished;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBoltUI;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUpFinished;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUpUI;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;
 
public class ChallengesMenuTab extends Fragment implements AdapterView.OnItemClickListener {
	private ListView challengeMenu;
    private MyAdapter adapter;
    Random rand = new Random();
	
	private static final String ID_Usain_Bolt 		= "1";
	private static final String ID_Wake_Me_Up 		= "2";
	private static final String ID_Can_You_Play 	= "3";
	private static final String ID_Calla_Al_Perro 	= "4";
//	private static final String ID_				 	= "5";
//	private static final String ID_				 	= "6";
//	private static final String ID_				 	= "7";
//	private static final String ID_				 	= "8";
//	private static final String ID_				 	= "9";
//	private static final String ID_				 	= "10";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.challenge_menu_tab, container, false);
        
        ((Home)getActivity()).refreshButton.setVisibility(View.INVISIBLE);
        
        challengeMenu = (ListView) rootView.findViewById(R.id.challengesMenu_list);
		challengeMenu.setOnItemClickListener(this);
        
        DataManager dm = (DataManager) DataManager.getInstance();
        List <DTState> challenges = dm.getChallenges();
        int total_challenges = challenges.size();
		DTState[] items=new DTState[total_challenges];
		
		for(int index=0;index<total_challenges;index++){
            items[index] = challenges.get(index);
        }
		
        adapter = new MyAdapter(getActivity().getApplicationContext(),items);
        challengeMenu.setAdapter(adapter);
        
        return rootView;
    }
    
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	Intent challenge = null;
		if(adapter.getItem(position).getChallengeId().equals(ID_Usain_Bolt)){
			if(adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(), UsainBoltUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(), UsainBoltFinished.class);
		} else if (adapter.getItem(position).getChallengeId().equals(ID_Wake_Me_Up)){
			if(adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(), WakeMeUpUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(), WakeMeUpFinished.class);
		} else if (adapter.getItem(position).getChallengeId().equals(ID_Can_You_Play)){
			if(adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(), CanYouPlayUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(), CanYouPlayFinished.class);
		} else if (adapter.getItem(position).getChallengeId().equals(ID_Calla_Al_Perro)){
			if(adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(), ShutTheDogUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(), ShutTheDogFinished.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
/*		} else if (adapter.getItem(position).getChallengeId().equals(ID_)){
			challenge = new Intent(getActivity().getApplicationContext(), UI.class);
*/
		}
    	startActivity(challenge);
    	this.getActivity().finish();
	}
    
    private class MyAdapter extends ArrayAdapter<DTState>{
		private final Context context;
		private final DTState[] objects;

        public MyAdapter(Context context, DTState[] objects) {
            super(context, R.layout.challenge_list_item, objects);
            this.context = context;
            this.objects = objects;   
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          LayoutInflater inflater = (LayoutInflater) context
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View rowView = inflater.inflate(R.layout.challenge_list_item, parent, false);
          
          TextView challengeName = (TextView) rowView.findViewById(R.id.challengeName_list);
          ImageView challengeIcon = (ImageView) rowView.findViewById(R.id.challengeIcon);
          TextView challengeDescription = (TextView) rowView.findViewById(R.id.challengeDescription);
          
          DTState s = objects[position];
          if (s.getChallengeId().equals(ID_Usain_Bolt)) {
        	challengeName.setText(getResources().getString(R.string.usain_bolt));
			challengeIcon.setImageResource(R.drawable.ic_usain_bolt);
            switch(s.getChallengeLevel()){
	            case 1: challengeDescription.setText(R.string.description_usain_bolt_1);break;
	            case 2: challengeDescription.setText(R.string.description_usain_bolt_2);break;
            }
          } else if (s.getChallengeId().equals(ID_Wake_Me_Up)){
        	  challengeName.setText(getResources().getString(R.string.wake_me_up));
			challengeIcon.setImageResource(R.drawable.ic_despertame_a_tiempo);
			switch(s.getChallengeLevel()){
	            case 1: challengeDescription.setText(s.getChallengeName() + " description");break;
	            case 2: challengeDescription.setText(s.getChallengeName() + " description");break;
	        }
          } else if (s.getChallengeId().equals(ID_Can_You_Play)){
        	  challengeName.setText(getResources().getString(R.string.can_you_play));
			challengeIcon.setImageResource(R.drawable.ic_can_you_play);
			switch(s.getChallengeLevel()){
	            case 1: challengeDescription.setText(s.getChallengeName() + " description");break;
	            case 2: challengeDescription.setText(s.getChallengeName() + " description");break;
	        }
          } else if (s.getChallengeId().equals(ID_Calla_Al_Perro)){
        	  challengeName.setText(getResources().getString(R.string.shut_the_dog));
			challengeIcon.setImageResource(R.drawable.ic_calla_al_perro);
			switch(s.getChallengeLevel()){
	            case 1: challengeDescription.setText(s.getChallengeName() + " description");break;
	            case 2: challengeDescription.setText(s.getChallengeName() + " description");break;
	        }
/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
			/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
			/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
			/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
			/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
			/*          } else if (s.getChallengeId().equals(ID_)){
  			challengeIcon.setImageResource(R.drawable.ic_);
  			switch(s.getChallengeLevel()){
  	            case 1: challengeDescription.setText(R.string.description_);break;
  	            case 2: challengeDescription.setText(R.string.description_);break;
  	        }*/
          } 
          else {
        	challengeName.setText("Challenge "+ position);
			challengeIcon.setImageResource(R.drawable.ic_launcher);
			challengeDescription.setText("Challenge "+ position + " description");
          }
          if(s.getCurrentAttempt() == 0)
        	  rowView.setBackgroundColor(getResources().getColor(R.color.blanco));
          else{
        	  rowView.setBackgroundColor(getResources().getColor(R.color.gris));
        	  ((ImageView)rowView.findViewById(R.id.tick)).setVisibility(View.VISIBLE);
          }
        	  

          return rowView;
        }
    }
 
}
