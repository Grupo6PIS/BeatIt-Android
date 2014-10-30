package com.g6pis.beatitapp.challenges.wakemeup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class WakeMeUpUI extends Activity implements SensorEventListener, OnClickListener {
	private static final String CHALLENGE_ID = "2";
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	
	private DTDateTime dateTimeStart;
	
	private boolean timerRunning = false;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 300;
	
	private Integer level;
	private Integer number_of_repetitions;
	//private Integer number_of_repetitions_LEVEL1 = 3;
	//private Integer number_of_repetitions_LEVEL2 = 4;
	private static final long TIME_LEVEL1_1 = 5;
	private static final long TIME_LEVEL1_2 = 4;
	//private static final long TIME_LEVEL1_3 = 3;
	private static final long TIME_LEVEL2_1 = 9;
	private static final long TIME_LEVEL2_2 = 7;
	private static final long TIME_LEVEL2_3 = 5;
	//private static final long TIME_LEVEL2_4 = 3;
	private long hidden_secs = 0; // This value can be modified in order to change the challenge difficulty
	
	private static final long TOLERANCE = 500; // In milliseconds
	private static final long MAX = 10000; // This is the limit to stop the counter automatically because the player has already lost	
	private long time = 0; // Real countdown
	private long g_millis = 0; // Fictitious countdown because it includes the MAX value
	private static final long INITIAL_COUNTER_VALUE = 10000; // In milliseconds
	
	private TextView startButton;
	private TextView textViewTimeLeftValue;
	private TextView textViewResult;

	private MediaPlayer mp_success;
	private MediaPlayer mp_fail;
	
	private CountDownTimer timer;
	private long attemps = 0;
	private long MAX_ATTEMPS = 3;
	
	private WakeMeUp wakeMeUp;
	private DTState state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wake_me_up);
		
		this.editActionBar(); 
		this.viewAssignment();
		
		wakeMeUp = (WakeMeUp) Factory.getInstance().getIDataManager().getChallenge(CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		
		mp_success = MediaPlayer.create(this, R.raw.success);
		mp_fail = MediaPlayer.create(this, R.raw.fail);
		
		
		switch (wakeMeUp.getLevel()) {

			case 1: {
				((TextView) findViewById(R.id.textView_Description_Value))
						.setText(getResources().getString(R.string.description_wake_me_up_1));
			}
			break;
			case 2: {
				((TextView) findViewById(R.id.textView_Description_Value))
						.setText(getResources().getString(R.string.description_wake_me_up_2));
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
			number_of_repetitions = wakeMeUp.getNumber_of_repetitions();
			hidden_secs = wakeMeUp.getHidden_secs();
			
			startButton = (Button) findViewById(R.id.start_button_wake_me_up);
			startButton.setOnClickListener(this);
			
			this.setDateTimeStart(Factory.getInstance().getIDataManager().getCurrentRound().getDateTimeStart());
			((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this.getDateTimeStart().toString());
			((TextView) findViewById(R.id.textView_Duration_Value))
			.setText(this.getDurationString());
	        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
			
			String t_initial_counter_value = String.format("%d", INITIAL_COUNTER_VALUE/1000);
			
			textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
			textViewTimeLeftValue.setText(t_initial_counter_value);
			
			textViewResult = (TextView) findViewById(R.id.textView_Result);
									
			timer = this.createTimer();
		}
		else {
			completeChallenge();
		}
		
	}
	
	
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(INITIAL_COUNTER_VALUE+MAX, 10) {
			
			public void onTick(long millisUntilFinished) {
				
				long millis = millisUntilFinished;
				g_millis = millis;
				String hms;
				if (millis > hidden_secs * 1000 + MAX) {
					hms = String.format("%d", 
							TimeUnit.MILLISECONDS.toSeconds(millis-MAX));
				}
				else {
					hms = String.format("??");
				}
				textViewTimeLeftValue.setText(hms);
			}

			public void onFinish() {
				if (timerRunning) {
					stopTimer();
					textViewTimeLeftValue.setText("Demoraste mucho !");
				}
			}
		};
		
		return timer;
	}
	
	@Override
	public void onClick(View v) {
		if (!timerRunning) {
			switch (v.getId()) {
				case R.id.start_button_wake_me_up: {
					startButton.setVisibility(View.INVISIBLE);
					textViewTimeLeftValue.setVisibility(View.VISIBLE);
					
					Handler handler = new Handler(); 
				    handler.postDelayed(new Runnable() { 
				         public void run() { 
				        	 textViewResult.setVisibility(View.INVISIBLE); 
				         } 
				    }, 1000); 
					this.timer.start();
					this.timerRunning = true;
				}
				break;
			}
		}
	}
	
	public void viewAssignment() {
		startButton = (TextView) findViewById(R.id.start_button_wake_me_up);
		textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
		textViewResult = (TextView) findViewById(R.id.textView_Result);
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
		wakeMeUp.reset();
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
			wakeMeUp.reset();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Customize ActionBar
	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.wake_me_up)));
	}
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
	private void stopTimer() {
		timerRunning = false;
		time = g_millis - MAX;
		textViewResult.setVisibility(View.VISIBLE);
		if (Math.abs(time) < TOLERANCE) {
			wakeMeUp.setSucceed_times(wakeMeUp.getSucceed_times() + 1);
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
		
		timer.cancel();

		number_of_repetitions--;
		if (number_of_repetitions > 0) {
			switch (getLevel()) {
				case 1: {
					switch (number_of_repetitions) {
						case 1: {
							hidden_secs = TIME_LEVEL1_1;
						}
							break;
						case 2: {
							hidden_secs = TIME_LEVEL1_2;
						}
							break;
					}
				}
					break;
				case 2: {
					switch (number_of_repetitions) {
						case 1: {
							hidden_secs = TIME_LEVEL2_1;
						}
							break;
						case 2: {
							hidden_secs = TIME_LEVEL2_2;
						}
							break;
						case 3: {
							hidden_secs = TIME_LEVEL2_3;
						}
							break;
					}
				}
					break;	
			}
			this.createTimer();
			
			startButton.setVisibility(View.VISIBLE);
			textViewTimeLeftValue.setVisibility(View.INVISIBLE);
			startButton.setText(getResources().getString(R.string.restart));
			
		}
		else {
			completeChallenge();
		}
	}
	

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
	    Sensor mySensor = sensorEvent.sensor;
	    
	    if (timerRunning) {
	    	if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		        float x = sensorEvent.values[0];
		        float y = sensorEvent.values[1];
		        float z = sensorEvent.values[2];
		 
		        long curTime = System.currentTimeMillis();
		 
		        if ((curTime - lastUpdate) > 100) {
		            long diffTime = (curTime - lastUpdate);
		            lastUpdate = curTime;
		 
		            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
		            last_x = x;
		            last_y = y;
		            last_z = z;
		            if (speed > SHAKE_THRESHOLD) {
		            	stopTimer();
		            }
		 
		        }
		    }
	    }
	}
	
	public void completeChallenge() {
		senSensorManager.unregisterListener(this);
		timer = null;
		
		wakeMeUp.finishChallenge();

		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
		wakeMeUp.reset();
		
		// Activity Challenge Finished
		Intent finished = new Intent(this, WakeMeUpFinished.class);

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
	
	public String getDurationString() {

		double finishSeconds = state.getFinishSeconds();
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		Date date = cal.getTime();
		double currentSeconds = date.getTime() / 1000;

		double duration = (finishSeconds - currentSeconds);
		String result = "";
		int d = ((int) duration);
		if ((duration / 60) > 1) {
			duration = Math.ceil(duration / 60);
			if ((duration / 60) > 1) {
				duration = Math.ceil(duration / 60);
				if ((duration / 24) > 1) {
					duration = Math.ceil(duration / 24);
					if ((duration / 7) > 1) {
						duration = Math.ceil(duration / 7);
						if (duration > 1)
							result = ((int) duration) + " "
									+ getResources().getString(R.string.weeks);
						else
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.week);
					} else {
						if (duration > 1)
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.days);
						else
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.day);
					}
				} else {
					if (duration > 1)
						result = ((int) duration)+ " "
								+ getResources().getString(R.string.hours);
					else
						result = ((int) duration)+ " "
								+ getResources().getString(R.string.hour);
				}
			} else {
				if (duration > 1)
					result = ((int) duration)+ " "
							+ getResources().getString(R.string.minutes);
				else
					result = ((int) duration)+ " "
							+ getResources().getString(R.string.minute);
			}
		} else {
			result = getResources().getString(R.string.few_seconds);
		}
		return result;
	}
	
}