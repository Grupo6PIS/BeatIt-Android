package com.g6pis.beatit.challenges.despertameatiempo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.g6pis.beatit.Home;
import com.g6pis.beatit.R;

public class DespertameATiempoFinished extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despertame_a_tiempo_finished);

		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(this.getString(R.string.app_name));
       /* findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);*/
		
		//TODO pedirle el state correspondiente al DataManager
		/*((TextView) findViewById(R.id.sms_sent_value))
				.setText(getIntent().getExtras().getString("sms"));

		((TextView) findViewById(R.id.fb_post_value))
				.setText(getIntent().getExtras().getString("fb"));

		((TextView) findViewById(R.id.textView_Score_Value))
				.setText(getIntent().getExtras().getString("score") + getResources().getString(R.string.points));*/
		
		/*((TextView) findViewById(R.id.textView_Start_Time_Value))
		.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		((TextView)findViewById(R.id.textView_Duration_Value)).setText(finishDate.toString());*/

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
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
	}
	
}
