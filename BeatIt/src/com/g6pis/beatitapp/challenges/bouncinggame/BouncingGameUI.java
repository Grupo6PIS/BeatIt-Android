package com.g6pis.beatitapp.challenges.bouncinggame;

import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.persistence.StateDAO;

public class BouncingGameUI extends Activity implements SensorEventListener, OnClickListener {
	private static final String CHALLENGE_ID = "5";
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	
	private DTDateTime dateTimeStart;
	
	private boolean timerRunning = false;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 300;
	
	private Integer level;
	
	private TextView startButton;
	
	private static final long INITIAL_COUNTER_VALUE = 60000; // In milliseconds
	
	private CountDownTimer timer;
	//private double score = 0;
	//private long succeed_times = 0;
	private long attemps = 0;
	private long MAX_ATTEMPS = 3;

	private MediaPlayer mp_success;
	private MediaPlayer mp_fail;
	
	private static BouncingGame bouncingGame;
	private DTState state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bouncing_game);
		
		this.editActionBar(); 
		this.viewAssignment();
		
		bouncingGame = (BouncingGame) DataManager.getInstance().getChallenge(CHALLENGE_ID);
		state = DataManager.getInstance().getState(CHALLENGE_ID);
		
		mp_success = MediaPlayer.create(this, R.raw.success);
		mp_fail = MediaPlayer.create(this, R.raw.fail);
		
		DataManager dm = DataManager.getInstance();
		
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
			this.setLevel(((DTState) dm.getState(CHALLENGE_ID)).getChallengeLevel());
			
			startButton = (Button) findViewById(R.id.start_challenge_button);
			startButton.setOnClickListener(this);
			
			this.setDateTimeStart(dm.getCurrentRound().getDateTimeStart());
			((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this.getDateTimeStart().toString());
			((TextView) findViewById(R.id.textView_Duration_Value))
			.setText(this.getDurationString());
	        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
			
			String t_initial_counter_value = String.format("%d", INITIAL_COUNTER_VALUE/1000);
			
			timer = this.createTimer();
		}
		else {
			completeChallenge();
		}
		
	}
	
	
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(INITIAL_COUNTER_VALUE, 10) {
			
			public void onTick(long millisUntilFinished) {
				
				long millis = millisUntilFinished;
				String hms;
				if (millis <= 0) {
					hms = String.format("%d", 
							TimeUnit.MILLISECONDS.toSeconds(millis));
				}
				else {
					hms = String.format("??");
				}
				//textViewTimeLeftValue.setText(hms);
			}

			public void onFinish() {
				if (timerRunning) {
					bouncingGame.finishChallenge();
					stopTimer();
				}
			}
		};
		
		return timer;
	}
	
	@Override
	public void onClick(View v) {
		if (!timerRunning) {
			switch (v.getId()) {
				case R.id.start_challenge_button: {
					startButton.setVisibility(View.INVISIBLE);
					this.timer.start();
					this.timerRunning = true;
					
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
	
	private void stopTimer() {
		timerRunning = false;
/*		time = g_millis - MAX;
		textViewResult.setVisibility(View.VISIBLE);
		if (Math.abs(time) < TOLERANCE) {
			bouncingGame.setSucceed_times(bouncingGame.getSucceed_times() + 1);
			// Show performance
			textViewResult.setText(getResources().getString(R.string.success));
			textViewResult.setBackgroundColor(getResources().getColor(R.color.verde));
			mp_success.start();
		}
		else {
			// Show performance
			textViewResult.setText(getResources().getString(R.string.fail));
			textViewResult.setBackgroundColor(getResources().getColor(R.color.red));
			mp_fail.start();
		}
		
*/		
		//textViewResult.setVisibility(View.VISIBLE);
		//textViewResult.setText(Double.toString(time));
		timer.cancel();

		completeChallenge();
	}
	

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
	}
	
	public static BouncingGame getBouncingGameInstance () {
		return bouncingGame;
	}
	
	public void completeChallenge() {
		senSensorManager.unregisterListener(this);
		timer = null;
		
		bouncingGame.setSucceed_times(bouncingGame.getSucceed_times() + 1);
		System.out.println("----- Succeed: " + bouncingGame.getSucceed_times());
		
		//bouncingGame.setSucceed_times(getSucceed_times());
		bouncingGame.finishChallenge();
		
		setAttemps(getAttemps()+1);
		

		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(DataManager.getInstance().getState(CHALLENGE_ID));
		bouncingGame.reset();
		
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
