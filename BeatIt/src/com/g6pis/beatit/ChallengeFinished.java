package com.g6pis.beatit;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChallengeFinished extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);

		double maxSpeed = getIntent().getExtras().getDouble("maxSpeed");
		double avgSpeed = getIntent().getExtras().getDouble("avgSpeed");
		double score = getIntent().getExtras().getDouble("score");
		
		((TextView) findViewById(R.id.textView_max_speed_value)).setText(Double
				.toString(maxSpeed) + "km/h");
		
		((TextView) findViewById(R.id.textView_avg_speed_value)).setText(Double
				.toString(avgSpeed) + "km/h");
		
		((TextView) findViewById(R.id.textView_Score_Value)).setText(Double
				.toString(score) + " puntos");


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge_finished, menu);
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
