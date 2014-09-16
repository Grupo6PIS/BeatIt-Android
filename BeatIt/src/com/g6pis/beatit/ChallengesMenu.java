package com.g6pis.beatit;

import java.util.Random;

import com.g6pis.beatit.challenges.UsainBolt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

public class ChallengesMenu extends Activity implements AdapterView.OnItemClickListener {

	private ListView challengeMenu;
    private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenges_menu);
		
		challengeMenu = (ListView) findViewById(R.id.challengesMenu_list);
		challengeMenu.setOnItemClickListener(this);
		
		String[] items=new String[10];
		items[0] = "Usain Bolt";
        for(int index=1;index<=9;index++){
            items[index] = "Challenge "+index;
        }
        adapter = new MyAdapter(this,items);
        challengeMenu.setAdapter(adapter);
		
		
		getActionBar().setHomeButtonEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenges_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(adapter.getItem(position).equals("Usain Bolt")){
			
			Intent challenge = new Intent(this, UsainBolt.class);
			
			Random rand = new Random();
	        final int  level = rand.nextInt(2) + 1;
	        
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
