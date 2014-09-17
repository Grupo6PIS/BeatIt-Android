package com.g6pis.beatit.challenges;

import java.util.HashSet;
import java.util.Set;

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
	private CountDownTimer timer;
	private boolean timerRunning = false;
	private boolean running = false;
	
	private Dialog settingsDialog;
	private Dialog speedDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_in_progress);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);

		this.settingsDialog = onCreateDialog(SETTINGS_DIALOG);
		
		
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			settingsDialog.show();
		}


		
		
		//TODO cargar el nivel del desafio segun la BD
		this.setLevel(getIntent().getExtras().getInt("level"));
		this.setChallengeId(getIntent().getExtras().getInt("challengeId"));

			
		switch (this.getLevel()) {

		case 1: {
			minSpeed = MIN_SPEED_LEVEL1;
			time = TIME_LEVEL1;
			((TextView)findViewById(R.id.textView_Description_Value_2)).setText(R.string.description_usain_bolt_1);
			((TextView)findViewById(R.id.textView_Time_Left_Value)).setText(R.string.time_left_value_1);
			
		}
			break;
		case 2: {
			minSpeed = MIN_SPEED_LEVEL2;
			time = TIME_LEVEL2;
			((TextView)findViewById(R.id.textView_Description_Value_2)).setText(R.string.description_usain_bolt_2);
			((TextView)findViewById(R.id.textView_Time_Left_Value)).setText(R.string.time_left_value_2);
		}
			break;
		}
		
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

		((TextView) findViewById(R.id.textView_Speed_Value)).setText(Double
				.toString(Math.round(this.getSpeed())) + " km/h");
		
		/*if((this.getSpeed() > 0) && (!this.running))
			this.speedDialog.show();
		if((this.getSpeed() == 0) && (!this.running)){
			this.running = true;
			this.speedDialog.hide();
		}*/
		
		if ((this.getSpeed() >= minSpeed)
				//&&(running)
				) {
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
				this.running = false;
				this.speedDialog.show();
				((TextView) findViewById(R.id.textView_Time_Left_Value))
						.setText(Long.toString(this.getTime()/ 1000) + " seconds");
			}
		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		this.settingsDialog.show();

	}

	@Override
	public void onProviderEnabled(String arg0) {
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

		
		
		//Abro la activity de desafío finalizado
		Intent challengeFinished = new Intent(this, ChallengeFinished.class);
		challengeFinished.putExtra("maxSpeed", Double.toString(Math.round(this.getMaxSpeed())));
		challengeFinished.putExtra("avgSpeed", Double.toString(Math.round(this.getAvgSpeed())));
		challengeFinished.putExtra("score", Double.toString(Math.round(this.getScore())));
		
		Log.d("maxSpeed", Double.toString(challengeFinished.getExtras().getDouble("maxSpeed")));
		Log.d("avgSpeed", Double.toString(challengeFinished.getExtras().getDouble("avgSpeed")));
		Log.d("score", Double.toString(challengeFinished.getExtras().getDouble("score")));
		
		startActivity(challengeFinished);

		//TODO ver si esto funciona correctamente...
		UsainBoltDAO db = new UsainBoltDAO(this);
		db.addUsainBolt(this);
//		UsainBolt challenge = (UsainBolt) db.getUsainBolt(0);
//		challenge.setMaxSpeed(this.getMaxSpeed());
//		challenge.setAvgSpeed(this.getAvgSpeed());
//		challenge.setScore(this.getScore());
//		db.updateChallenge(challenge);
		
		this.finish();
		

	}


	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  switch (id) {
	    case SETTINGS_DIALOG:{
	    builder.setMessage(R.string.gps_disabled);
	    builder.setCancelable(true);
	    builder.setPositiveButton(R.string.ok, new OkOnClickListener());
	    builder.setNegativeButton(R.string.cancel, new CancelOnClickListener());
	    return builder.create();
	    }
	    
	    case SPEED_DIALOG:{
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

	@Override
	public String toString() {

		return "UsainBolt[speed=" + this.getSpeed() + ", score="
				+ this.getScore() + "]" + super.toString();

	}

}
