package com.g6pis.beatit.challenges.callaalperro;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.g6pis.beatit.ChallengeFinished;
import com.g6pis.beatit.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CallaAlPerroUI extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private MediaPlayer mp;
	private Timer timerContador;
	
  	int rangoMin = 5;
  	int rangoMax = 15;
	private int segundosTranscurridos;
	int demoraMaxima = 5;
	
	boolean gano = false;
	double score = 0;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calla_al_perro);
		
		this.editActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calla_al_perro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void comenzar(View v){
		// Genera un numero random
		 mp = MediaPlayer.create(this, R.raw.bark);
		 mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		  mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	   	 
	   	 segundosTranscurridos = 0;
	
	   	 Random r = new Random();
	   	 int i1 = r.nextInt(rangoMax - rangoMin + 1) + rangoMin;
	   	 int tiempo = i1 * 1000;
	   	 new CountDownTimer(tiempo, 1000) {
	
	   	     public void onTick(long millisUntilFinished) {
	   	     }
	
	   	     public void onFinish() {
	   	    	 mSensorManager.registerListener(CallaAlPerroUI.this, mSensor,
	   	    			 SensorManager.SENSOR_DELAY_NORMAL);
	   	    	 if (mp != null){
	   	    		mp.start();
	   	    		
	   	    		///arranco el timer que cuenta cuanto pasa
	   	    		timerContador = new Timer();
	   	    		timerContador.scheduleAtFixedRate(new TimerTask() {         
	   	    	        @Override
	   	    	        public void run() {
	   	    	            runOnUiThread(new Runnable()
	   	    	            {
	   	    	                @Override
	   	    	                public void run()
	   	    	                {
	   	    	                    segundosTranscurridos++;
	   	    	                    if (segundosTranscurridos >= (demoraMaxima*1000)){
	   	    	                    	timerContador.cancel();
	   	    	                    	gano = false;
	   	    	                    }
	   	    	                }
	   	    	            });
	   	    	        }
	   	    	    }, 0, 1);
	   	    	 }
	   	     }
	   	     
	   	  }.start();
	}

	 protected void onResume() {
	  super.onResume();
	  
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
		
	 protected void onPause() {
	  super.onPause();
	  if (mSensorManager != null){
		  mSensorManager.unregisterListener(this);
	  }
	 }

	 public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 }


	 public void onSensorChanged(SensorEvent event) {
	  if (event.values[0] == 0) {
	   //iv.setImageResource(R.drawable.near);
		  if (mp != null){
			  mp.stop();
			  timerContador.cancel();
			  gano = true;
			  
			  //TERMINA EL DESAFIO
			  //Calculo el puntaje
			  score = segundosTranscurridos;
			  
			  //Abro el desafio finalizado
			  Intent callaAlPerroFinished = new Intent(this, ChallengeFinished.class);
			  callaAlPerroFinished.putExtra("resultado",
						this.gano);
			  callaAlPerroFinished.putExtra("score",
						Double.toString(this.score));
			  startActivity(callaAlPerroFinished);
			  this.finish();
		  }
	  } else {
		  //Esta lejos
	  }
	 }
}
