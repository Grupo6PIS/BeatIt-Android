package com.g6pis.beatit.challenges.despertameatiempo;

import java.util.concurrent.TimeUnit;
import java.lang.Math;

import com.g6pis.beatit.R;
import android.app.Activity;
import android.content.Context;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despertame_a_tiempo);
		
		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(this);
		
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
				textViewResult.setText("Perdiste :(");
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

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
	private void frenarTimer() {
		textViewResult.setVisibility(View.VISIBLE);
		String texto;
		time = g_millis - tope;
		if (TimeUnit.MILLISECONDS.toMillis(time) >= 0) {
			texto = String.format("+%d,%d", 
					Math.abs(TimeUnit.MILLISECONDS.toSeconds(time)),
					Math.abs(TimeUnit.MILLISECONDS.toMillis(time) - TimeUnit.MILLISECONDS.toSeconds(time) * 1000));
		}
		else {
			texto = String.format("-%d,%d", 
					Math.abs(TimeUnit.MILLISECONDS.toSeconds(time)),
					Math.abs(TimeUnit.MILLISECONDS.toMillis(time) - TimeUnit.MILLISECONDS.toSeconds(time) * 1000));
		}
		textViewTimeLeftValue.setText(texto);
		
		if (TimeUnit.MILLISECONDS.toMillis(g_millis - tope) < tolerancia && TimeUnit.MILLISECONDS.toMillis(g_millis - tope) > -tolerancia) {
			textViewResult.setText("Ganaste ;)");
		}
		else {
			texto = String.format("+%d", g_millis - tope);
			textViewResult.setText("Perdiste :(");
		}
		timer.cancel();
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
}
