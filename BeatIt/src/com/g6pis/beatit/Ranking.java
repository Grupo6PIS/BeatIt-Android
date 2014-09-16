package com.g6pis.beatit;

import java.util.List;





import com.g6pis.beatit.challenges.UsainBolt;
import com.g6pis.beatit.persistence.UsainBoltDAO;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Ranking extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking, menu);
		
		UsainBoltDAO db = new UsainBoltDAO(this);
    	
		List<UsainBolt> challenges = db.getAllUsainBolt();
		
		String ranking = "Ranking :";
		
		
		for(UsainBolt challenge : challenges){
			
			ranking += challenge.toString();
			
		}
		
		((TextView) findViewById(R.id.ranking_label)).setText(ranking);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
