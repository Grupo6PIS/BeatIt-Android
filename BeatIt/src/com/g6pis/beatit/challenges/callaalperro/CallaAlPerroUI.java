package com.g6pis.beatit.challenges.callaalperro;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.g6pis.beatit.Home;
import com.g6pis.beatit.R;
import com.g6pis.beatit.datatypes.DTDateTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
	
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	
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
		
		this.setDateTimeStart(this.getDateExtras(getIntent().getExtras()));

		this.editActionBar();
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

	public Timer getTimerContador() {
		return timerContador;
	}

	public void setTimerContador(Timer timerContador) {
		this.timerContador = timerContador;
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

	public int getRangoMin() {
		return rangoMin;
	}

	public void setRangoMin(int rangoMin) {
		this.rangoMin = rangoMin;
	}

	public int getRangoMax() {
		return rangoMax;
	}

	public void setRangoMax(int rangoMax) {
		this.rangoMax = rangoMax;
	}

	public int getSegundosTranscurridos() {
		return segundosTranscurridos;
	}

	public void setSegundosTranscurridos(int segundosTranscurridos) {
		this.segundosTranscurridos = segundosTranscurridos;
	}

	public int getDemoraMaxima() {
		return demoraMaxima;
	}

	public void setDemoraMaxima(int demoraMaxima) {
		this.demoraMaxima = demoraMaxima;
	}

	public boolean isGano() {
		return gano;
	}

	public void setGano(boolean gano) {
		this.gano = gano;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
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
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.callaalperro)));
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
			  Intent callaAlPerroFinished = new Intent(this, CallaAlPerroFinished.class);
			  callaAlPerroFinished.putExtra("resultado", this.gano);
			  callaAlPerroFinished.putExtra("score", Double.toString(this.score));			  
			  
			Calendar calendar = new GregorianCalendar();
			
			callaAlPerroFinished.putExtra("seconds", calendar.get(Calendar.SECOND));
			callaAlPerroFinished.putExtra("minutes", calendar.get(Calendar.MINUTE));
			callaAlPerroFinished.putExtra("hours", calendar.get(Calendar.HOUR_OF_DAY));
			callaAlPerroFinished.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
			callaAlPerroFinished.putExtra("month", calendar.get(Calendar.MONTH));
			callaAlPerroFinished.putExtra("year", calendar.get(Calendar.YEAR));
			callaAlPerroFinished.putExtra("dateTimeStart", dateTimeStart.toString());
			startActivity(callaAlPerroFinished);
			this.finish();
		  }
	  } else {
		  //Esta lejos
	  }
	 }
	 
	 @Override
		public void onBackPressed() {
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

}
