package com.g6pis.beatit;

import com.g6pis.beatit.datatypes.DTDateTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ChallengeFinished extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);

		// double maxSpeed = getIntent().getExtras().getString("maxSpeed");
		// double avgSpeed = getIntent().getExtras().getString("avgSpeed");
		// double score = getIntent().getExtras().getString("score");
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(this.getString(R.string.app_name));
        findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
		
		
		((TextView) findViewById(R.id.textView_max_speed_value))
				.setText(getIntent().getExtras().getString("maxSpeed") + "km/h");

		((TextView) findViewById(R.id.textView_avg_speed_value))
				.setText(getIntent().getExtras().getString("avgSpeed") + "km/h");

		((TextView) findViewById(R.id.textView_Score_Value))
				.setText(getIntent().getExtras().getString("score") + " puntos");
		
		((TextView) findViewById(R.id.textView_Start_Time_Value))
		.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		((TextView)findViewById(R.id.textView_Duration_Value)).setText(finishDate.toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.challenge_finished, menu);
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
	
	@Override
	public void onClick(View v) {
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
	}
	
}
