package com.g6pis.beatitapp.challenges.bouncinggame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;

public class BouncingGameUI extends Activity implements SensorEventListener, OnClickListener {
	private static final String CHALLENGE_ID = "5";
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	
	private DTDateTime dateTimeStart;
	
	private boolean timerRunning = false;
	private Integer level;
	
	private TextView startButton;
	
	private long attemps = 0;
	private long MAX_ATTEMPS = 3;

	private static BouncingGame bouncingGame;
	private DTState state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bouncing_game);
		
		this.editActionBar(); 
		this.viewAssignment();
		
		bouncingGame = (BouncingGame) Factory.getInstance().getIDataManager().getChallenge(CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		
		switch (bouncingGame.getLevel()) {

			case 1: {
				((TextView) findViewById(R.id.textView_Description_Value))
						.setText(getResources().getString(R.string.description_bouncing_game_1));
			}
			break;
			case 2: {
				((TextView) findViewById(R.id.textView_Description_Value))
						.setText(getResources().getString(R.string.description_bouncing_game_2));
			}
			break;
		}
		
		if(state.getMaxScore() > 0)
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setText(Double.toString(state.getMaxScore()));
		else{
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setVisibility(View.INVISIBLE);
			((TextView)findViewById(R.id.textView_To_Beat)).setVisibility(View.INVISIBLE);
		}
		
		if (attemps < MAX_ATTEMPS) {
			this.setLevel(((DTState) Factory.getInstance().getIDataManager().getState(CHALLENGE_ID)).getChallengeLevel());
			
			startButton = (Button) findViewById(R.id.start_challenge_button);
			startButton.setOnClickListener(this);
			
			this.setDateTimeStart(Factory.getInstance().getIDataManager().getCurrentRound().getDateTimeStart());
			((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this.getDateTimeStart().toString());
			((TextView) findViewById(R.id.textView_Duration_Value))
			.setText(this.getDurationString());
	        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
		}
		else {
			completeChallenge();
		}
	}
	
	@Override
	public void onClick(View v) {
		if (!timerRunning) {
			switch (v.getId()) {
				case R.id.start_challenge_button: {
					startButton.setVisibility(View.INVISIBLE);
					
					Intent home = new Intent(this, BouncingGameUI2.class);
					startActivity(home);
					this.finish();
				}
				break;
			}
		}
	}

	public void viewAssignment() {
		startButton = (TextView) findViewById(R.id.start_challenge_button);
	}
	
	protected void onPause() {
	    super.onPause();
	    senSensorManager.unregisterListener(this);
	}
	
	protected void onResume() {
	    super.onResume();
	    senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onBackPressed(){
		timerRunning = false;
		bouncingGame.reset();
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
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
			bouncingGame.reset();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Customize ActionBar
	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bouncing)));
	}
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
	}
	
	public static BouncingGame getBouncingGameInstance () {
		return bouncingGame;
	}
	
	public void completeChallenge() {
		// Activity Challenge Finished
		Intent finished = new Intent(this, BouncingGameFinished.class);

		startActivity(finished);
		this.finish();
	}

	public long getAttemps() {
		return attemps;
	}

	public void setAttemps(long attemps) {
		this.attemps = attemps;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}
	
	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}
	
	public String getDurationString(){
		String result = "";
		DTDateTime now = new DTDateTime();
		
		String difference = state.getDateTimeFinish().diff(now);
		Integer diff = state.getDateTimeFinish().diff(now, difference);
		
		if(difference.equals("year"))
			if(diff > 1)
				result = diff + " " + getResources().getString(R.string.years);
			else
				result = diff + " " + getResources().getString(R.string.year);
		
		if(difference.equals("month"))
			if(diff > 1)
				result = diff + " " + getResources().getString(R.string.months);
			else
				result = diff + " " + getResources().getString(R.string.month);

		if(difference.equals("day"))
			if(diff > 1){
				if(diff > 6){
					if(diff > 13){
						diff = (int) Math.ceil(diff/7);
						result = diff + " " + getResources().getString(R.string.weeks);
					}else{
						diff = (int) Math.ceil(diff/7);
						result = diff + " " + getResources().getString(R.string.week);
					}
						
				}else
					result = diff + " " + getResources().getString(R.string.days);
			}else
				result = diff + " " + getResources().getString(R.string.day);

		if(difference.equals("hour"))
			if(diff > 1)
				result = diff + " " + getResources().getString(R.string.hours);
			else
				result = diff + " " + getResources().getString(R.string.hour);
		
		if(difference.equals("minute"))
			if(diff > 1)
				result = diff + " " + getResources().getString(R.string.minutes);
			else
				result = diff + " " + getResources().getString(R.string.minute);
		
		if(difference.equals("second"))
			if(diff > 1)
				result = diff + " " + getResources().getString(R.string.seconds);
			else
				result = diff + " " + getResources().getString(R.string.second);
		
		return result;
        
    }
}
