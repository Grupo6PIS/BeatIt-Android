package com.g6pis.beatit.challenges;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.g6pis.beatit.entities.Challenge;
import com.g6pis.beatit.persistence.UsainBoltDAO;
import com.g6pis.beatit.R;

public class UsainBolt extends Challenge implements OnClickListener,
		LocationListener {

	private LocationManager locationManager;

	private static final long MIN_TIME = 0;
	private static final float MIN_DISTANCE = 0;
	private static final double MIN_SPEED_LEVEL1 = 20.0;
	private static final double MIN_SPEED_LEVEL2 = 25;
	private static final long TIME_LEVEL1 = 30000;
	private static final long TIME_LEVEL2 = 60000;

	private int longitude;
	private int latitude;
	private double speed;
	private double avgSpeed;
	private Set<Double> speeds = new HashSet<Double>();

	private double score = 0;
	private double maxSpeed = 0;
	private int challengeId;
	private Integer level;
	private double minSpeed;
	private long time;
	private CountDownTimer timer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.challenge_in_progress);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);

		// TODO controlar GPS y enviar al settings si no está conectado
		// findViewById(R.id.settings_button).setOnClickListener(this);

		switch (this.getLevel()) {

		case 1: {
			minSpeed = MIN_SPEED_LEVEL1;
			time = TIME_LEVEL1;
		}
			break;
		case 2: {
			minSpeed = MIN_SPEED_LEVEL2;
			time = TIME_LEVEL2;
		}
			break;
		}

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
		this.setAvgSpeed(this.getSpeeds());

		if (this.getSpeed() > this.getMaxSpeed())
			this.setMaxSpeed(this.getSpeed());

		((TextView) findViewById(R.id.textView_Speed_Value)).setText(Double
				.toString(this.getSpeed()));

		if (this.getSpeed() >= minSpeed) {
			// TODO probar timer
			// TODO controlar velocidad inicial sea 0
			if (timer == null) {
				timer = new CountDownTimer(time, 1000l) {
					public void onTick(long millisUntilFinished) {
						((TextView) findViewById(R.id.textView_Time_Left_Value))
								.setText(Long
										.toString(millisUntilFinished / 1000)
										+ " seconds");
					}

					public void onFinish() {
						completeChallenge();
						Toast.makeText(getApplicationContext(),
								"Challenge Completed!", Toast.LENGTH_SHORT)
								.show();
					}
				}.start();
			}

		} else {
			if (timer != null) {
				timer = null;
				this.setMaxSpeed(0);
				this.speeds = new HashSet<Double>();
				this.setAvgSpeed(this.getSpeeds());
				this.setScore(0);
			}
		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

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
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public void completeChallenge() {
		locationManager.removeUpdates(this);
		timer = null;
		this.setScore((this.getMaxSpeed() + this.getAvgSpeed()) * 2);

		// TODO guardar en la BD el puntaje

		// UsainBoltDAO db = new UsainBoltDAO(this);
		//
		// UsainBolt challenge = (UsainBolt) db.getUsainBolt(0);
		// challenge.setLatitude(this.getLatitude());
		// challenge.setLongitude(this.getLongitude());
		// challenge.setSpeed(this.getSpeed());
		// challenge.setScore(this.getScore());
		//
		// db.updateChallenge(challenge);

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

	public void setAvgSpeed(Set<Double> speeds) {
		double avgSpeed = 0;
		for (double speed : speeds) {
			avgSpeed += speed;
		}

		avgSpeed /= speeds.size();

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

	@Override
	public String toString() {

		return "UsainBolt[speed=" + this.getSpeed() + ", score="
				+ this.getScore() + "]" + super.toString();

	}

}
