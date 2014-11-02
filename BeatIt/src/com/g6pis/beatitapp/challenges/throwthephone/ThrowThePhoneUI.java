package com.g6pis.beatitapp.challenges.throwthephone;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

public class ThrowThePhoneUI extends Activity implements SensorEventListener, OnClickListener {
	private String CHALLENGE_ID = "6";

	private double GRAVITATION_ACELERATION = -9.80665f;
	private static final int ATTENTION_DIALOG = 70;

	private boolean isInTheAir;
	private boolean challengeStarted;
	private long startTime;
	private long endTime;

	private DTState state;
	private ThrowThePhone throwThePhone;
	
	private Dialog attentionDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throw_the_phone);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		throwThePhone = (ThrowThePhone) Factory.getInstance().getIDataManager().getChallenge(
				CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);

		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setTextColor(Color.parseColor(throwThePhone.getColor()));
		((Button) findViewById(R.id.start_challenge_button))
				.setBackgroundColor(Color.parseColor(throwThePhone.getColor()));
		((Button) findViewById(R.id.start_challenge_button)).setOnClickListener(this);

		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value)).setText(this
				.getDurationString());

		if (state.getMaxScore() > 0)
			((TextView) findViewById(R.id.textView_To_Beat_Value))
					.setText(Double.toString(state.getMaxScore()));
		else {
			((TextView) findViewById(R.id.textView_To_Beat_Value))
					.setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.textView_To_Beat))
					.setVisibility(View.INVISIBLE);

		}

		switch (throwThePhone.getLevel()) {
		case 1:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.descritpion_throw_the_phone_1));
			break;
		case 2:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.descritpion_throw_the_phone_2));
			break;
		}
		this.editActionBar();

		this.isInTheAir = false;
		this.challengeStarted = false;
		
		this.attentionDialog = onCreateDialog(ATTENTION_DIALOG);
		attentionDialog.hide();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sm.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(this);
		super.onStop();
	}

	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setLogo(getResources().getDrawable(R.drawable.app_logo));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(throwThePhone.getColor())));

	}

	@Override
	public void onBackPressed() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(this);
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
			SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
			sm.unregisterListener(this);
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		this.attentionDialog.show();

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

	@SuppressLint("UseValueOf")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (challengeStarted) {
			double normaVector = Math.sqrt(event.values[0] * event.values[0]
					+ event.values[1] * event.values[1] + event.values[2]
					* event.values[2]);

			if (!this.isInTheAir && Math.abs(normaVector) < 2) {
				this.isInTheAir = true;
				this.startTime = System.currentTimeMillis();
			} else {
				if (this.isInTheAir
						&& Math.abs(normaVector + this.GRAVITATION_ACELERATION) < 2) {
					this.isInTheAir = false;
					this.endTime = System.currentTimeMillis();
					float tiempo = this.endTime - this.startTime;
					tiempo = tiempo / 2; // Obtenemos el tiempo de subida (o
											// bajada)
					tiempo = tiempo / 1000; // lo pasamos a segundos.
					Double height = (-tiempo * tiempo
							* this.GRAVITATION_ACELERATION / 2) * 100;
					throwThePhone.setHeightReached(height.intValue());
					
					this.completeChallenge();

				}
			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	public void completeChallenge(){
		Intent finished = new Intent(this, ThrowThePhoneFinished.class);
		startActivity(finished);
		this.finish();
		throwThePhone.finishChallenge();
		StateDAO db = new StateDAO(this);
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
		SharedPreferences sharedPrefs = getApplicationContext()
				.getSharedPreferences("asdasd_preferences",
						Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
		editor.commit();
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case ATTENTION_DIALOG:
			builder.setMessage(R.string.attention_text);
			builder.setTitle(getResources().getString(
					R.string.attention));
			builder.setCancelable(true);
			builder.setNegativeButton(R.string.im_scared,
					new CancelOnClickListener());
			builder.setPositiveButton(R.string.come_on,
					new OkOnClickListener());
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			((Button) findViewById(R.id.start_challenge_button)).setText(R.string.start_challenge_button);
			((Button) findViewById(R.id.start_challenge_button)).setClickable(true);
			
			
		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			((Button) findViewById(R.id.start_challenge_button)).setText(R.string.ready_to_fly);
			((Button) findViewById(R.id.start_challenge_button)).setClickable(false);
			challengeStarted = true;
		}
	}

}
