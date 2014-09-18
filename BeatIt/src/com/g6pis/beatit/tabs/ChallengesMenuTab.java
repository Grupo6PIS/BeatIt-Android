package com.g6pis.beatit.tabs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import com.g6pis.beatit.R;
import com.g6pis.beatit.challenges.UsainBolt;
import com.g6pis.beatit.datatypes.DTDateTime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
 
public class ChallengesMenuTab extends Fragment implements AdapterView.OnItemClickListener {
	private ListView challengeMenu;
    private MyAdapter adapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.challenge_menu_tab, container, false);
        
        
        challengeMenu = (ListView) rootView.findViewById(R.id.challengesMenu_list);
		challengeMenu.setOnItemClickListener(this);
		
		String[] items=new String[10];
		items[0] = "Usain Bolt";
        for(int index=1;index<=9;index++){
            items[index] = "Challenge "+index;
        }
        adapter = new MyAdapter(getActivity().getApplicationContext(),items);
        challengeMenu.setAdapter(adapter);
        
        return rootView;
    }
    
    
    
    
    
    
    
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(adapter.getItem(position).equals("Usain Bolt")){
			
			Intent challenge = new Intent(getActivity().getApplicationContext(), UsainBolt.class);
			
			Random rand = new Random();
	        final int  level = rand.nextInt(2) + 1;
	        
        	

        	Calendar calendar = new GregorianCalendar();

        	challenge.putExtra("seconds",calendar.get(Calendar.SECOND));
        	challenge.putExtra("minutes",calendar.get(Calendar.MINUTE));
        	challenge.putExtra("hours",calendar.get(Calendar.HOUR));
        	challenge.putExtra("day",calendar.get(Calendar.DAY_OF_MONTH));
        	challenge.putExtra("month",calendar.get(Calendar.MONTH));
        	challenge.putExtra("year",calendar.get(Calendar.YEAR));
        	challenge.putExtra("level", level);
        	challenge.putExtra("challengeId", 0);
        	
        	startActivity(challenge);
		}
		
	}
    
    private class MyAdapter extends ArrayAdapter<String>{
		private final Context context;
		private final String[] objects;

        public MyAdapter(Context context, String[] objects) {
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
          
          challengeName.setText(objects[position]);
          // change the icon for Windows and iPhone
          String s = objects[position];
          if (s.equals("Usain Bolt")) {
            challengeIcon.setImageResource(R.drawable.ic_usain_bolt);
            challengeDescription.setText(R.string.description_usain_bolt_2);
          } else {
            challengeIcon.setImageResource(R.drawable.ic_launcher);
            challengeDescription.setText(s + " description");
          }

          return rowView;
        }
    }
 
}
