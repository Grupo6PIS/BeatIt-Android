package com.g6pis.beatitapp.challenges.usainbolt;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;







//import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;
//import com.g6pis.beatit.persistence.UsainBoltDAO;

public class UsainBoltUI extends Activity implements OnClickListener,
		LocationListener {
	
	private static final String CHALLENGE_ID = "1";
	
	private static final int CONFIRMATION_DIALOG = 60;
	
	private static final long MIN_TIME = 0;
	private static final float MIN_DISTANCE = 0;
	private static final int SETTINGS_DIALOG = 10;
	private static final int SPEED_DIALOG = 20;
	private static final int MAX_ATTEMPTS = 3;


	private LocationManager locationManager;

	private CountDownTimer timer;
	private boolean timerRunning = false;
	private boolean challengeStarted = false;

	private Dialog settingsDialog;
	private Dialog speedDialog;

	private TextView startChallengeButton;
	//private ImageView homeButton;
//	private ImageView cancelButton;
	private TextView textViewSpeedValue;
	private TextView textViewTimeLeftValue;
	
	private UsainBolt usainBolt;
	private DTState state;

	/***Activity Functions***/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usain_bolt);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.editActionBar();
		this.viewAssignment();

		/*homeButton.setOnClickListener(this);
		homeButton.setVisibility(View.VISIBLE);*/
//		cancelButton.setOnClickListener(this); 
		startChallengeButton.setOnClickListener(this);

		this.settingsDialog = onCreateDialog(SETTINGS_DIALOG);
		this.speedDialog = onCreateDialog(SPEED_DIALOG);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			settingsDialog.show();
			startChallengeButton.setClickable(false);
		}

		usainBolt = (UsainBolt) Factory.getInstance().getIDataManager().getChallenge(
				CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		 

		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value))
		.setText(this.getDurationString());
		
		if(state.getMaxScore() > 0)
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setText(Double.toString(state.getMaxScore()));
		else{
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setVisibility(View.INVISIBLE);
			((TextView)findViewById(R.id.textView_To_Beat)).setVisibility(View.INVISIBLE);
			
		}
		

		switch (usainBolt.getLevel()) {

		case 1: {
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(R.string.description_usain_bolt_1);
			textViewTimeLeftValue.setText(R.string.time_left_value_1);
		}
			break;
		case 2: {
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(R.string.description_usain_bolt_2);
			textViewTimeLeftValue.setText(R.string.time_left_value_2);
		}
			break;
		}

		timer = this.createTimer();

	}
	
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			settingsDialog.show();
			startChallengeButton.setClickable(false);
		} else
			startChallengeButton.setClickable(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		settingsDialog.dismiss();
		speedDialog.dismiss();
		locationManager.removeUpdates(this);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.challenge_in_progress, menu);
		return true;
	}
	/***Activity Functions***/
	
	
	
	/***Useful Functions***/
	public DTDateTime getDateExtras(Bundle extras) {
		DTDateTime date = new DTDateTime();

		date.setDay(extras.getInt("day"));
		date.setMonth(extras.getInt("month"));
		date.setYear(extras.getInt("year"));
		date.setHour(extras.getInt("hours"));
		date.setMinute(extras.getInt("minutes"));
		date.setSecond(extras.getInt("seconds"));

		return date;
	}
	
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(usainBolt.getTime(), 1000l) {
			public void onTick(long millisUntilFinished) {
				textViewTimeLeftValue.setText(
						getResources().getString(R.string.time_left) + " "
						+ Double.toString(millisUntilFinished/1000)
						+ " " + getResources().getString(R.string.seconds));
			}

			public void onFinish() {
				usainBolt.finishChallenge();
				if (Factory.getInstance().getIDataManager().getHaveToSendScore()) {
					Thread t = new Thread() {
						public void run() {

							Factory.getInstance().getIDataManager().sendScore();
							Factory.getInstance().getIDataManager().updateRanking();
						}
					};

					t.start();
				}
				StateDAO db = new StateDAO(getApplicationContext());
				db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
				SharedPreferences sharedPrefs = getApplicationContext()
						.getSharedPreferences("asdasd_preferences",
								Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
				editor.commit();
				completeChallenge();
			}
		};
		
		return timer;
	}
	
	public void viewAssignment() {
		startChallengeButton = (TextView) findViewById(R.id.start_challenge_button);
		//homeButton = (ImageView) findViewById(R.id.homeButton);
//		cancelButton = (ImageView) findViewById(R.id.cancelButton);
		textViewSpeedValue = (TextView) findViewById(R.id.textView_Friends_Value);
		textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
	}
	
	public void completeChallenge() {
		locationManager.removeUpdates(this);
		timer = null;
		
		// Activity Challenge Finished
		Intent challengeFinished = new Intent(this, UsainBoltFinished.class);
		startActivity(challengeFinished);

		this.finish();


	}
	/***Useful Functions***/
	
	
	// Customize ActionBar
	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
	}
	
	//Buttons onClick Function
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.cancelButton: {
			this.setMaxSpeed(0);
			this.setAvgSpeed(0.0);
			this.completeChallenge();
		}
			break;*/
		/*case R.id.homeButton: {
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
		}
			break;*/
		case R.id.start_challenge_button: {
			startChallengeButton.setVisibility(View.INVISIBLE);
			textViewSpeedValue.setVisibility(View.VISIBLE);
			textViewTimeLeftValue.setVisibility(View.VISIBLE);
//			cancelButton.setVisibility(View.VISIBLE);
			challengeStarted = true;
			textViewSpeedValue.setText(getResources().getString(R.string.speed)
					+ " 0.0 km/h");

		}
			break;
		}

	}
	
	@Override
	public void onBackPressed(){
		
		usainBolt.reset();
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	usainBolt.reset();
	    	Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	/***GPS Functions***/
	@Override
	public void onLocationChanged(Location loc) {
		Double speed = loc.getSpeed() * 3.6;
		usainBolt.addSpeed(speed);

		String speedValue = getResources().getString(R.string.speed)
				+ Double.toString(Math.round(speed)) + " km/h";

		textViewSpeedValue.setText(speedValue);

		if ((speed > 0) && (!this.challengeStarted)) {
			this.speedDialog.show();
			startChallengeButton.setClickable(false);
			startChallengeButton.setVisibility(View.VISIBLE);
			textViewSpeedValue.setVisibility(View.INVISIBLE);
			textViewTimeLeftValue.setVisibility(View.INVISIBLE);
//			cancelButton.setVisibility(View.INVISIBLE);
		}
		
		if ((speed == 0) && (!this.challengeStarted)) {
			this.speedDialog.hide();
			startChallengeButton.setClickable(true);
		}

		if ((speed >= usainBolt.getMinSpeed()) && (challengeStarted)) {
			if (!timerRunning) {
				this.timer.start();
				this.timerRunning = true;
			}
			
		} else {
			if (this.timerRunning) {
				timer.cancel();
				this.timerRunning = false;
				usainBolt.setMaxSpeed(0);
				usainBolt.setAvgSpeed(0);
				usainBolt.finishChallenge();
				if (Factory.getInstance().getIDataManager().getHaveToSendScore()) {
					Thread t = new Thread() {
						public void run() {

							Factory.getInstance().getIDataManager().sendScore();
							Factory.getInstance().getIDataManager().updateRanking();
						}
					};

					t.start();
				}
				StateDAO db = new StateDAO(this);
				db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
				SharedPreferences sharedPrefs = getApplicationContext()
						.getSharedPreferences("asdasd_preferences",
								Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
				editor.commit();
				this.challengeStarted = false;
				textViewTimeLeftValue.setText(
						getResources().getString(R.string.time_left) + " "
						+ Double.toString(usainBolt.getTime()/1000)
						+ " " + getResources().getString(R.string.seconds));
				if (state.isFinished()) {
					completeChallenge();
				}
			}
		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		this.settingsDialog.show();
		startChallengeButton.setClickable(false);
		startChallengeButton.setVisibility(View.VISIBLE);
		textViewSpeedValue.setVisibility(View.INVISIBLE);
		textViewTimeLeftValue.setVisibility(View.INVISIBLE);
//		cancelButton.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onProviderEnabled(String arg0) {
		startChallengeButton.setClickable(true);
		this.settingsDialog.hide();

	}
	/***GPS Functions***/
	
	
	/***Dialogs Functions***/
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case SETTINGS_DIALOG: {
			builder.setMessage(R.string.gps_disabled);
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok, new OkOnClickListener());
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}

		case SPEED_DIALOG: {
			builder.setMessage(R.string.speed_greater_than_zero);
			builder.setTitle(getResources().getString(R.string.app_name));
			return builder.create();
		}

		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
	}
	/***Dialogs Functions***/
	
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
