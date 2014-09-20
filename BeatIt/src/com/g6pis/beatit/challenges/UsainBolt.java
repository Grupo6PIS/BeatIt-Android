package com.g6pis.beatit.challenges;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.entities.Challenge;
import com.g6pis.beatit.persistence.ChallengeDAO;
import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatit.ChallengeFinished;
import com.g6pis.beatit.Home;
//import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatit.R;

public class UsainBolt extends Challenge implements OnClickListener,
		LocationListener {

	private static final long MIN_TIME = 0;
	private static final float MIN_DISTANCE = 0;
	private static final double MIN_SPEED_LEVEL1 = 20.0;
	private static final double MIN_SPEED_LEVEL2 = 25.0;
	private static final long TIME_LEVEL1 = 30000;
	private static final long TIME_LEVEL2 = 90000;
	private static final int SETTINGS_DIALOG = 10;
	private static final int SPEED_DIALOG = 20;
	private static final int MAX_ATTEMPTS = 3;

	private int challengeId;
	private Integer level;
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;

	private double minSpeed;
	private long time;
	
	private double speed;
	private Set<Double> speeds = new HashSet<Double>();
	private LocationManager locationManager;
	
	private double avgSpeed;
	private double score = 0;
	private double maxSpeed = 0;
	private int attempts = 0;

	private CountDownTimer timer;
	private boolean timerRunning = false;
	private boolean challengeStarted = false;

	private Dialog settingsDialog;
	private Dialog speedDialog;

	private Button startChallengeButton;
	private ImageView homeButton;
	private ImageView cancelButton;
	private TextView textViewSpeedValue;
	private TextView textViewTimeLeftValue;
	
	/***Activity Functions***/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_in_progress);

		this.editActionBar();
		this.viewAssignment();

		homeButton.setOnClickListener(this);
		homeButton.setVisibility(View.VISIBLE);
		cancelButton.setOnClickListener(this);
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

		// TODO cargar el nivel del desafio segun la BD
		this.setLevel(getIntent().getExtras().getInt("level"));
		this.setChallengeId(getIntent().getExtras().getInt("challengeId"));
		this.setDateTimeStart(this.getDateExtras(getIntent().getExtras()));

		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this
				.getDateTimeStart().toString());

		switch (this.getLevel()) {

		case 1: {
			minSpeed = MIN_SPEED_LEVEL1;
			time = TIME_LEVEL1;
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(R.string.description_usain_bolt_1);
			textViewTimeLeftValue.setText(R.string.time_left_value_1);

		}
			break;
		case 2: {
			minSpeed = MIN_SPEED_LEVEL2;
			time = TIME_LEVEL2;
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
		CountDownTimer timer = new CountDownTimer(time, 1000l) {
			public void onTick(long millisUntilFinished) {
				textViewTimeLeftValue.setText(getResources().getString(
						R.string.time_left)
						+ Double.toString(Math
								.round(millisUntilFinished / 1000))
						+ " " + R.string.seconds);
			}

			public void onFinish() {
				completeChallenge();
				Toast.makeText(getApplicationContext(), "Challenge Completed!",
						Toast.LENGTH_SHORT).show();
			}
		};
		
		return timer;
	}
	
	public void viewAssignment() {
		startChallengeButton = (Button) findViewById(R.id.start_challenge_button);
		homeButton = (ImageView) findViewById(R.id.homeButton);
		cancelButton = (ImageView) findViewById(R.id.cancelButton);
		textViewSpeedValue = (TextView) findViewById(R.id.textView_Speed_Value);
		textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
	}
	
	public void completeChallenge() {
		locationManager.removeUpdates(this);
		timer = null;

		// Calculating the average speed
		Double averageSpeed = 0.0;
		for (Double speed : this.getSpeeds()) {
			averageSpeed += speed;
		}
		averageSpeed /= this.getSpeeds().size();
		this.setAvgSpeed(averageSpeed);

		// Calculating the score
		this.setScore((this.getMaxSpeed() + this.getAvgSpeed()) * 2);

		// Activity Challenge Finished
		Intent challengeFinished = new Intent(this, ChallengeFinished.class);
		challengeFinished.putExtra("maxSpeed",
				Double.toString(Math.round(this.getMaxSpeed())));
		challengeFinished.putExtra("avgSpeed",
				Double.toString(Math.round(this.getAvgSpeed())));
		challengeFinished.putExtra("score",
				Double.toString(Math.round(this.getScore())));

		Calendar calendar = new GregorianCalendar();

		challengeFinished.putExtra("seconds", calendar.get(Calendar.SECOND));
		challengeFinished.putExtra("minutes", calendar.get(Calendar.MINUTE));
		challengeFinished.putExtra("hours", calendar.get(Calendar.HOUR));
		challengeFinished.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
		challengeFinished.putExtra("month", calendar.get(Calendar.MONTH));
		challengeFinished.putExtra("year", calendar.get(Calendar.YEAR));
		challengeFinished.putExtra("dateTimeStart", dateTimeStart.toString());

		startActivity(challengeFinished);

		// Store the finished challenge
		// TODO verificar puntaje m�ximo
		UsainBoltDAO db = new UsainBoltDAO(this);
		db.addUsainBolt(this);

		this.finish();

	}
	/***Useful Functions***/
	
	
	// Customize ActionBar
	public void editActionBar() {
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
		case R.id.cancelButton: {
			this.setMaxSpeed(0);
			this.setAvgSpeed(0.0);
			this.completeChallenge();
		}
			break;
		case R.id.homeButton: {
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
		}
			break;
		case R.id.start_challenge_button: {
			startChallengeButton.setVisibility(View.INVISIBLE);
			textViewSpeedValue.setVisibility(View.VISIBLE);
			textViewTimeLeftValue.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);
			challengeStarted = true;
			textViewSpeedValue.setText(getResources().getString(R.string.speed)
					+ " 0.0 km/h");
		}
			break;
		}

	}
	
	
	/***GPS Functions***/
	@Override
	public void onLocationChanged(Location loc) {

		this.setSpeed(loc.getSpeed() * 3.6);
		this.getSpeeds().add(this.getSpeed());

		if (this.getSpeed() > this.getMaxSpeed())
			this.setMaxSpeed(this.getSpeed());

		String speedValue = getResources().getString(R.string.speed)
				+ Double.toString(Math.round(this.getSpeed())) + " km/h";

		textViewSpeedValue.setText(speedValue);

		if ((this.getSpeed() > 0) && (!this.challengeStarted)) {
			this.speedDialog.show();
			startChallengeButton.setClickable(false);
			startChallengeButton.setVisibility(View.VISIBLE);
			textViewSpeedValue.setVisibility(View.INVISIBLE);
			textViewTimeLeftValue.setVisibility(View.INVISIBLE);
			cancelButton.setVisibility(View.INVISIBLE);
		}
		
		if ((this.getSpeed() == 0) && (!this.challengeStarted)) {
			this.speedDialog.hide();
			startChallengeButton.setClickable(true);
		}

		if ((this.getSpeed() >= minSpeed) && (challengeStarted)) {
			if (!timerRunning) {
				this.timer.start();
				this.timerRunning = true;
			}
			
		} else {
			if (this.timerRunning) {
				timer.cancel();
				this.timerRunning = false;
				this.setMaxSpeed(0);
				this.speeds = new HashSet<Double>();
				this.challengeStarted = false;
				attempts++;
				textViewTimeLeftValue.setText(getResources().getString(
						R.string.time_left)
						+ Double.toString(time/1000)
						+ " seconds");
				if (attempts == MAX_ATTEMPTS) {
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
		cancelButton.setVisibility(View.INVISIBLE);

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
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok, new OkOnClickListener());
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}

		case SPEED_DIALOG: {
			builder.setMessage(R.string.speed_greater_than_zero);
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
	
	
	/***Getter & Setters***/
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Set<Double> getSpeeds() {
		return speeds;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public double getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(double minSpeed) {
		this.minSpeed = minSpeed;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}

	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	public DTDateTime getDateTimeFinish() {
		return dateTimeFinish;
	}

	public void setDateTimeFinish(DTDateTime dateTimeFinish) {
		this.dateTimeFinish = dateTimeFinish;
	}
	/***Getters & Setters***/
	

}
