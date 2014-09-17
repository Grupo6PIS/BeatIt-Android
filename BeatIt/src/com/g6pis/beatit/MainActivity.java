package com.g6pis.beatit;



import java.util.Random;

import com.g6pis.beatit.challenges.UsainBolt;
import com.g6pis.beatit.persistence.UsainBoltDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
//        Random rand = new Random();
//        final int  level = rand.nextInt(2) + 1;
        Intent login = new Intent(MainActivity.this, Login.class);
    	this.startActivity(login);
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent challengeMenu = new Intent(MainActivity.this, ChallengesMenu.class);
            	startActivity(challengeMenu);
            }
        });
        
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
//            	Intent challenge = new Intent(MainActivity.this, Ranking.class);
//            	startActivity(challenge);
            }
        });
        
        getActionBar().setHomeButtonEnabled(false);
        
        UsainBoltDAO db = new UsainBoltDAO(this);
        
        
		UsainBolt challenge = new UsainBolt();
		challenge.setMaxSpeed(20);
		challenge.setAvgSpeed(20.0);
		challenge.setScore(200);
		db.addUsainBolt(challenge);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
   

	
    
   

    
}
