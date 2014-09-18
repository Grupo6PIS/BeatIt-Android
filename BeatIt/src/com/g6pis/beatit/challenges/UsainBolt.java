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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.entities.Challenge;
import com.g6pis.beatit.persistence.ChallengeDAO;
import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatit.ChallengeFinished;
//import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatit.R;

public class UsainBolt extends Challenge implements OnClickListener,
		LocationListener {

	private LocationManager locationManager;

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

	private int longitude;
	private int latitude;
	private double speed;
	private double avgSpeed;
	private Set<Double> speeds = new HashSet<Double>();

	private double score = 0;
	private double maxSpeed = 0;
	private double minSpeed;
	private long time;
	private CountDownTimer timer;
	private boolean timerRunning = false;
	private boolean challengeStarted = false;
	private int attempts = 0;

	private Dialog settingsDialog;
	private Dialog speedDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_in_progress);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setTitle(this.getString(R.string.app_name));
        actionBar.setHomeButtonEnabled(true);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);

		this.settingsDialog = onCreateDialog(SETTINGS_DIALOG);

		((Button) findViewById(R.id.start_challenge_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						((Button) findViewById(R.id.start_challenge_button))
								.setVisibility(View.INVISIBLE);
						((TextView) findViewById(R.id.textView_Speed_Value))
								.setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.textView_Time_Left_Value))
								.setVisibility(View.VISIBLE);
						challengeStarted = true;
						((TextView) findViewById(R.id.textView_Speed_Value))
						.setText(getResources().getString(R.string.speed) + " 0.0 km/h");
					}
				});

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			settingsDialog.show();
			((Button) findViewById(R.id.start_challenge_button))
					.setClickable(false);
		}

		// TODO cargar el nivel del desafio segun la BD
		DTDateTime startDate = new DTDateTime();
		startDate.setDay(getIntent().getExtras().getInt("day"));
		startDate.setMonth(getIntent().getExtras().getInt("month"));
		startDate.setYear(getIntent().getExtras().getInt("year"));
		startDate.setHour(getIntent().getExtras().getInt("hours"));
		startDate.setMinute(getIntent().getExtras().getInt("minutes"));
		startDate.setSecond(getIntent().getExtras().getInt("seconds"));

		this.setLevel(getIntent().getExtras().getInt("level"));
		this.setChallengeId(getIntent().getExtras().getInt("challengeId"));
		this.setDateTimeStart(startDate);

		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this
				.getDateTimeStart().toString());

		switch (this.getLevel()) {

		case 1: {
			minSpeed = MIN_SPEED_LEVEL1;
			time = TIME_LEVEL1;
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(R.string.description_usain_bolt_1);
			((TextView) findViewById(R.id.textView_Time_Left_Value))
					.setText(R.string.time_left_value_1);

		}
			break;
		case 2: {
			minSpeed = MIN_SPEED_LEVEL2;
			time = TIME_LEVEL2;
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(R.string.description_usain_bolt_2);
			((TextView) findViewById(R.id.textView_Time_Left_Value))
					.setText(R.string.time_left_value_2);
		}
			break;
		}

		timer = new CountDownTimer(time, 1000l) {
			public void onTick(long millisUntilFinished) {
				((TextView) findViewById(R.id.textView_Time_Left_Value))
						.setText(getResources().getString(R.string.time_left)
								+ Double.toString(Math.round(millisUntilFinished/1000)) + " seconds");
			}

			public void onFinish() {
				completeChallenge();
				Toast.makeText(getApplicationContext(), "Challenge Completed!",
						Toast.LENGTH_SHORT).show();
			}
		};

		this.speedDialog = onCreateDialog(SPEED_DIALOG);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@Override
	public void onLocationChanged(Location loc) {

		this.setLatitude((int) loc.getLatitude());
		this.setLongitude((int) loc.getLongitude());
		this.setSpeed(loc.getSpeed() * 3.6);
		this.getSpeeds().add(this.getSpeed());

		Double averageSpeed = 0.0;
		for (Double speed : this.getSpeeds()) {
			averageSpeed += speed;
		}

		averageSpeed /= this.getSpeeds().size();
		this.setAvgSpeed(averageSpeed);

		if (this.getSpeed() > this.getMaxSpeed())
			this.setMaxSpeed(this.getSpeed());

		((TextView) findViewById(R.id.textView_Speed_Value))
				.setText(getResources().getString(R.string.speed)
						+ Double.toString(Math.round(this.getSpeed()))
						+ " km/h");

		if ((this.getSpeed() > 0) && (!this.challengeStarted)) {
			this.speedDialog.show();
			((Button) findViewById(R.id.start_challenge_button))
					.setClickable(false);
			((Button) findViewById(R.id.start_challenge_button))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.textView_Speed_Value))
					.setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.textView_Time_Left_Value))
					.setVisibility(View.INVISIBLE);

		}
		if ((this.getSpeed() == 0) && (!this.challengeStarted)) {
			this.speedDialog.hide();
			((Button) findViewById(R.id.start_challenge_button))
					.setClickable(true);
		}

		if ((this.getSpeed() >= minSpeed) && (challengeStarted)) {
			// TODO probar timer
			// TODO controlar velocidad inicial sea 0
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
				this.setAvgSpeed(0d);
				this.setScore(0);
				this.challengeStarted = false;
				attempts++;
				if (attempts == MAX_ATTEMPTS) {
					completeChallenge();
				}
				// this.speedDialog.show();
				// ((TextView) findViewById(R.id.textView_Time_Left_Value))
				// .setText(Long.toString(this.getTime()/ 1000) + " seconds");
				//
				// ((Button)findViewById(R.id.start_challenge_button)).setClickable(false);
				// ((Button)findViewById(R.id.start_challenge_button)).setVisibility(View.VISIBLE);
				// ((TextView)findViewById(R.id.textView_Speed_Value)).setVisibility(View.INVISIBLE);
				// ((TextView)findViewById(R.id.textView_Time_Left_Value)).setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		this.settingsDialog.show();
		((Button) findViewById(R.id.start_challenge_button))
				.setClickable(false);
		((Button) findViewById(R.id.start_challenge_button))
				.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.textView_Speed_Value))
				.setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.textView_Time_Left_Value))
				.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onProviderEnabled(String arg0) {
		((Button) findViewById(R.id.start_challenge_button)).setClickable(true);
		this.settingsDialog.hide();

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
			((Button) findViewById(R.id.start_challenge_button))
					.setClickable(false);
		} else
			((Button) findViewById(R.id.start_challenge_button))
					.setClickable(true);
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
		getMenuInflater().inflate(R.menu.challenge_in_progress, menu);
		return true;
	}

	public void completeChallenge() {
		locationManager.removeUpdates(this);
		timer = null;
		this.setScore((this.getMaxSpeed() + this.getAvgSpeed()) * 2);

		// Abro la activity de desafío finalizado
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

		Log.d("maxSpeed",
				Double.toString(challengeFinished.getExtras().getDouble(
						"maxSpeed")));
		Log.d("avgSpeed",
				Double.toString(challengeFinished.getExtras().getDouble(
						"avgSpeed")));
		Log.d("score", Double.toString(challengeFinished.getExtras().getDouble(
				"score")));

		startActivity(challengeFinished);

		// TODO ver si esto funciona correctamente...
		UsainBoltDAO db = new UsainBoltDAO(this);
		db.addUsainBolt(this);
		// UsainBolt challenge = (UsainBolt) db.getUsainBolt(0);
		// challenge.setMaxSpeed(this.getMaxSpeed());
		// challenge.setAvgSpeed(this.getAvgSpeed());
		// challenge.setScore(this.getScore());
		// db.updateChallenge(challenge);

		this.finish();

	}

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

	/*
	 * Getter & Setters
	 */

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

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

	@Override
	public String toString() {

		return "UsainBolt[speed=" + this.getSpeed() + ", score="
				+ this.getScore() + "]" + super.toString();

	}

}
