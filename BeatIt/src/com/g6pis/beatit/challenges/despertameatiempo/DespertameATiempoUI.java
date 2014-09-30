package com.g6pis.beatit.challenges.despertameatiempo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

import com.g6pis.beatit.ChallengeFinished;
import com.g6pis.beatit.R;
import com.g6pis.beatit.challenges.invitefriends.CanYouPlayFinished;
import com.g6pis.beatit.datatypes.DTDateTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	
	private long tolerancia = 500; // En milisegundos
	private long segs_ocultos = 3; // Este valor se puede modificar para cambiar la dificultad
	private long tope = 10000; // Es el límite para detener el contador automáticamente porque ya perdió por mucho	
	private long time = 0; // Cuenta regresiva real
	private long g_millis = 0; // Cuenta regresiva ficticia porque incluye el valor del tope
	private long valor_inicial_contador = 10000; // En milisegundos
	
	private TextView startButton;
	private TextView textViewResult;
	private TextView textViewTimeLeftValue;
	
	private CountDownTimer timer;
	private double score = 0;
	private double tiempo = 0;
	private boolean gano = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despertame_a_tiempo);
		
		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(this);
		
		this.setDateTimeStart(this.getDateExtras(getIntent().getExtras()));
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(this
				.getDateTimeStart().toString());
		
		/* ACELEROMETRO */
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        /* FIN ACELEROMETRO */
		
		String t_valor_inicial_contador = String.format("%d", valor_inicial_contador/1000);
		
		textViewTimeLeftValue = (TextView) findViewById(R.id.textView_Time_Left_Value);
		textViewTimeLeftValue.setText(t_valor_inicial_contador);
		
		textViewResult = (TextView) findViewById(R.id.textView_Result);
		textViewResult.setText("Resultado");
		
		timer = this.createTimer();
		
	}
	
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
		CountDownTimer timer = new CountDownTimer(valor_inicial_contador+tope, 10) {
			
			public void onTick(long millisUntilFinished) {
				
				long millis = millisUntilFinished;
				g_millis = millis;
				String hms;
				if (millis > segs_ocultos * 1000 + tope) {
					hms = String.format("%d", 
							TimeUnit.MILLISECONDS.toSeconds(millis-tope));
				}
				else {
					hms = String.format("??");
				}
				
				System.out.println(hms);
				textViewTimeLeftValue.setText(hms);
			}

			public void onFinish() {
				frenarTimer();
				textViewTimeLeftValue.setText("Demoraste mucho !");
			}
		};
		
		return timer;
	}
	
	@Override
	public void onClick(View v) {
		if (!timerRunning) {
			switch (v.getId()) {
				case R.id.start_button: {
					startButton.setVisibility(View.INVISIBLE);
					textViewTimeLeftValue.setVisibility(View.VISIBLE);
					this.timer.start();
					this.timerRunning = true;
				}
				break;
			}
		}
	}
	
	public void viewAssignment() {
		startButton = (TextView) findViewById(R.id.start_button);
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
	

	// Customize ActionBar
	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
	}
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
	private void frenarTimer() {
		textViewResult.setVisibility(View.VISIBLE);
		String texto;
		time = g_millis - tope;
		long result_segs;
		long result_milis;
		System.out.println("--- Time: " + time);
		result_segs = Math.abs(TimeUnit.MILLISECONDS.toSeconds(time));
		result_milis = Math.abs(TimeUnit.MILLISECONDS.toMillis(time) - TimeUnit.MILLISECONDS.toSeconds(time) * 1000);
		setTiempo(result_segs, result_milis);
		System.out.println("--- Segs: " + result_segs);
		System.out.println("--- Milis: " + result_milis);
		
		if (TimeUnit.MILLISECONDS.toMillis(time) >= 0) {
			texto = String.format("+%d,%d", 
					result_segs,
					result_milis);
		}
		else {
			texto = String.format("-%d,%d", 
					result_segs,
					result_milis);
		}
		textViewTimeLeftValue.setText(texto);
		
		if (TimeUnit.MILLISECONDS.toMillis(g_millis - tope) < tolerancia && TimeUnit.MILLISECONDS.toMillis(g_millis - tope) > -tolerancia) {
			textViewResult.setText("Ganaste ;)");
			this.setGano(true);
		}
		else {
			textViewResult.setText("Perdiste :(");
			this.setGano(false);
		}
		timer.cancel();
		completeChallenge();
	}
	

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
	    Sensor mySensor = sensorEvent.sensor;
	    
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
	
	public void completeChallenge() {
		senSensorManager.unregisterListener(this);
		timer = null;
		
		// Calculating the score
		long veces_exito = 4;
		this.setScore((veces_exito)*20);

		// Activity Challenge Finished
		Intent despertameATiempoFinished = new Intent(this, DespertameATiempoFinished.class);
		System.out.println("--- Get Tiempo: " + getTiempo());
		despertameATiempoFinished.putExtra("resultado",
				this.getGano());
		despertameATiempoFinished.putExtra("avgSpeed",
				Double.toString(this.getTiempo()));
		despertameATiempoFinished.putExtra("score",
				Double.toString(Math.round(this.getScore())));

		startActivity(despertameATiempoFinished);

		this.finish();
	}
	
	public double getTiempo() {
		return tiempo;
	}

	public void setTiempo(double segs, double milis) {
		this.tiempo = segs + milis/1000;
		System.out.println("--- Tiempo: " + tiempo);
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public boolean getGano() {
		return gano;
	}

	public void setGano(boolean gano) {
		this.gano = gano;
	}
	
	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}
	
	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}
	
}
