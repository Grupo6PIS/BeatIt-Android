package com.g6pis.beatit.challenges.despertameatiempo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

import com.g6pis.beatit.Home;
import com.g6pis.beatit.R;
import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.datatypes.DTState;

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
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DespertameATiempoUI extends Activity implements SensorEventListener, OnClickListener {
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	
	private DTDateTime dateTimeStart;
	
	private boolean timerRunning = false;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 300;
	
	private static String challengeId = "2";
	private Integer level;
	private Integer cant_repeticiones;
	private Integer cant_repeticiones_LEVEL1 = 3;
	private Integer cant_repeticiones_LEVEL2 = 4;
	private static final long TIME_LEVEL1_1 = 5;
	private static final long TIME_LEVEL1_2 = 4;
	private static final long TIME_LEVEL1_3 = 3;
	private static final long TIME_LEVEL2_1 = 9;
	private static final long TIME_LEVEL2_2 = 7;
	private static final long TIME_LEVEL2_3 = 5;
	private static final long TIME_LEVEL2_4 = 3;
	private long segs_ocultos = 0; // Este valor se modifica para cambiar la dificultad
	
	private static final long TOLERANCIA = 500; // En milisegundos
	private static final long TOPE = 10000; // Es el límite para detener el contador automáticamente porque ya perdió por mucho	
	private long time = 0; // Cuenta regresiva real
	private long g_millis = 0; // Cuenta regresiva ficticia porque incluye el valor del tope
	private static final long VALOR_INICIAL_CONTADOR = 10000; // En milisegundos
	
	private TextView startButton;
	private TextView textViewTimeLeftValue;
	private TextView textViewResult;
	
	private CountDownTimer timer;
	private double score = 0;
	private long exitos = 0;
	private long attemps = 0;
	private long max_attemps = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despertame_a_tiempo);
		
		DataManager dm = DataManager.getInstance();
		
		if (attemps < max_attemps) {
			this.setLevel(((DTState) dm.getState(challengeId)).getChallengeLevel());
			switch (this.getLevel()) {
				case 1: {
					segs_ocultos = TIME_LEVEL1_3;
					cant_repeticiones = cant_repeticiones_LEVEL1;
				}
					break;
				case 2: {
					segs_ocultos = TIME_LEVEL2_4;
					cant_repeticiones = cant_repeticiones_LEVEL2;
				}
					break;	
			}
			
			startButton = (Button) findViewById(R.id.start_button_despertame);
			startButton.setOnClickListener(this);
			
			this.setDateTimeStart(dm.getCurrentRound().getDateTimeStart());
			((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this.getDateTimeStart().toString());
			
			/* ACELEROMETRO */
	        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
	        /* FIN ACELEROMETRO */
			
			String t_valor_inicial_contador = String.format("%d", VALOR_INICIAL_CONTADOR/1000);
			
			textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
			textViewTimeLeftValue.setText(t_valor_inicial_contador);
			
			textViewResult = (TextView) findViewById(R.id.textView_Result);
			
			editActionBar();
			
			timer = this.createTimer();
		}
		else {
			// INCOMPLETO PORQUE FALTA LA PERSISTENCIA LOCAL PARA CONTAR ATTEMPS !!!!!!!!
			completeChallenge();
		}
		
	}
	
	/***Useful Functions***/
	/*
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
	*/
	
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(VALOR_INICIAL_CONTADOR+TOPE, 10) {
			
			public void onTick(long millisUntilFinished) {
				
				long millis = millisUntilFinished;
				g_millis = millis;
				String hms;
				if (millis > segs_ocultos * 1000 + TOPE) {
					hms = String.format("%d", 
							TimeUnit.MILLISECONDS.toSeconds(millis-TOPE));
				}
				else {
					hms = String.format("??");
				}
				textViewTimeLeftValue.setText(hms);
			}

			public void onFinish() {
				frenarTimer();
				textViewTimeLeftValue.setText("Demoraste mucho !");
				textViewResult.setText("Demoraste mucho !");
			}
		};
		
		return timer;
	}
	
	@Override
	public void onClick(View v) {
		if (!timerRunning) {
			switch (v.getId()) {
				case R.id.start_button_despertame: {
					startButton.setVisibility(View.INVISIBLE);
					textViewTimeLeftValue.setVisibility(View.VISIBLE);
					textViewResult.setVisibility(View.INVISIBLE);
					this.timer.start();
					this.timerRunning = true;
				}
				break;
			}
		}
	}
	
	public void viewAssignment() {
		startButton = (TextView) findViewById(R.id.start_button_despertame);
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
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.despertame)));
	}
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
	private void frenarTimer() {
		timerRunning = false;
		time = g_millis - TOPE;
		if (Math.abs(time) < TOLERANCIA) {
			setCantExitos(getCantExitos() + 1);
		}			
		
		textViewResult.setVisibility(View.VISIBLE);
		textViewResult.setText(Double.toString(time));
		timer.cancel();
		
		cant_repeticiones--;
		if (cant_repeticiones > 0) {
			switch (getLevel()) {
				case 1: {
					switch (cant_repeticiones) {
						case 1: {
							segs_ocultos = TIME_LEVEL1_1;
						}
							break;
						case 2: {
							segs_ocultos = TIME_LEVEL1_2;
						}
							break;
					}
				}
					break;
				case 2: {
					switch (cant_repeticiones) {
						case 1: {
							segs_ocultos = TIME_LEVEL2_1;
						}
							break;
						case 2: {
							segs_ocultos = TIME_LEVEL2_2;
						}
							break;
						case 3: {
							segs_ocultos = TIME_LEVEL2_3;
						}
							break;
					}
				}
					break;	
			}
			this.createTimer();
			
			startButton.setVisibility(View.VISIBLE);
			textViewTimeLeftValue.setVisibility(View.INVISIBLE);
			
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
		            if (speed > SHAKE_THRESHOLD) {
		            	frenarTimer();
		            }
		 
		            last_x = x;
		            last_y = y;
		            last_z = z;
		        }
		    }
	    }
	}
	
	public void completeChallenge() {
		senSensorManager.unregisterListener(this);
		timer = null;
		
		setAttemps(getAttemps()+1);
		
		// Calculating the score
		this.setScore(getCantExitos()*20);

		// Activity Challenge Finished
		Intent despertameATiempoFinished = new Intent(this, DespertameATiempoFinished.class);
		despertameATiempoFinished.putExtra("cantExitos",
			Long.toString(this.getCantExitos()));
		despertameATiempoFinished.putExtra("score",
			Integer.toString((int) this.getScore()));
		despertameATiempoFinished.putExtra("dateTimeStart", getDateTimeStart().toString());
		despertameATiempoFinished.putExtra("attemps",
			Long.toString(Math.round(this.getAttemps())));
		
		Calendar calendar = new GregorianCalendar();

		despertameATiempoFinished.putExtra("seconds", calendar.get(Calendar.SECOND));
		despertameATiempoFinished.putExtra("minutes", calendar.get(Calendar.MINUTE));
		despertameATiempoFinished.putExtra("hours", calendar.get(Calendar.HOUR_OF_DAY));
		despertameATiempoFinished.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
		despertameATiempoFinished.putExtra("month", calendar.get(Calendar.MONTH));
		despertameATiempoFinished.putExtra("year", calendar.get(Calendar.YEAR));
		despertameATiempoFinished.putExtra("dateTimeStart", dateTimeStart.toString());

		startActivity(despertameATiempoFinished);

		this.finish();
	}
	
	public long getCantExitos() {
		return exitos;
	}

	public void setCantExitos(long exitos) {
		this.exitos = exitos;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
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
	
}