package com.g6pis.beatitapp.challenges.shutthedog;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;

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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ShutTheDogUI extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private MediaPlayer mp;
	private Handler handler = new Handler();
	
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	
	CountDownTimer c;
	
	int level = 0;
	//Rangos nivel 1
	int minRange1[] = {5,3,2};
	int maxRange1[] = {9,7,5};
	//Rangos nivel 2
	int minRange2[] = {2,3,4};
	int maxRange2[] = {5,6,7};
	//Array de resultados
	int results[] = new int[3];
	int it = 0;
	boolean firstTime = true;
	
	private int secondCount;
	int maxDelay = 5; //en segundos
	
	boolean hasWon = true;
	double score = 0;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shut_the_dog);
		
		this.setDateTimeStart(this.getDateExtras(getIntent().getExtras()));

		this.editActionBar();
		
		mp = MediaPlayer.create(this, R.raw.bark);
		mp.setLooping(true);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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

	public int getSecondCount() {
		return secondCount;
	}

	public void setSecondCount(int segundosTranscurridos) {
		this.secondCount = segundosTranscurridos;
	}

	public int getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(int demoraMaxima) {
		this.maxDelay = demoraMaxima;
	}

	public boolean isHasWon() {
		return hasWon;
	}

	public void setHasWon(boolean won) {
		this.hasWon = won;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public void comenzar(View v){
		Button b = (Button)findViewById(R.id.start_button);
		b.setText(R.string.shut_the_dog_started);
		b.setBackgroundColor(getResources().getColor(R.color.shutthedog));
		b.setEnabled(false);
		
		Random r = new Random();
		int segundosComienzo = r.nextInt(maxRange1[it] - minRange1[it] + 1) + minRange1[it];
		int tiempo = segundosComienzo * 1000;
		
		handler.postDelayed(rutine, tiempo);
	}
	
	private Runnable rutine = new Runnable() {
		   @Override
		   public void run() {
			   mSensorManager.registerListener(ShutTheDogUI.this, mSensor,
						SensorManager.SENSOR_DELAY_NORMAL);
				if (mp != null){
					mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					    @Override
					    public void onPrepared(MediaPlayer mp) {
					    	mp.start();
					    }});
					if (firstTime){
						mp.start();
						firstTime = false;						
					} else {
						try {
							mp.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				c = new CountDownTimer(maxDelay*1000, 1) {
					
					public void onTick(long millisUntilFinished) {
						secondCount++;
					}
					
					public void onFinish() {
						//PERDIO
						mSensorManager.unregisterListener(ShutTheDogUI.this, mSensor);
						
						mp.stop();
						it = 0;
						secondCount=0;
						
						Button b = (Button)findViewById(R.id.start_button);
						b.setText(R.string.shut_the_dog_lost);
						b.setBackgroundColor(getResources().getColor(R.color.red));
						b.setEnabled(true);
					}
					
				}.start();
		   }
		};

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
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shutthedog)));
	}
		
	 protected void onPause() {
	  super.onPause();
	  if (mSensorManager != null){
		  mSensorManager.unregisterListener(this);
	  }
	 }

	 public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 }
	 
	 public void onPrepared(MediaPlayer player) {
		 player.start();
	 }


	 public void onSensorChanged(SensorEvent event) {
	  if (event.values[0] == 0) {
		  if (mp != null){
			  mSensorManager.unregisterListener(ShutTheDogUI.this, mSensor);
			  mp.stop();
			  c.cancel();
			  
			  Button b = (Button)findViewById(R.id.start_button);
			  b.setText(it+1 + " !");
			  
			  results[it] = secondCount;
			  secondCount = 0;
			  
			  if (it <= 2){
				it++;
				if (it !=3){
					Random r = new Random();
					int segundosComienzo = r.nextInt(maxRange1[it] - minRange1[it] + 1) + minRange1[it];
					int tiempo = segundosComienzo * 1000;
					handler.postDelayed(rutine, tiempo);					
				}
			  }
			  
			  if(it == 3){
				  //GANO EL DESAFIO
				  //Calculo el puntaje
				  score = results[0] + results[1] + results[2];

				  //Abro el desafio finalizado
				  Intent shutTheDogFinished = new Intent(this, ShutTheDogFinished.class);
				  shutTheDogFinished.putExtra("resultado", this.hasWon);
				  shutTheDogFinished.putExtra("score", Double.toString(this.score));			  
				  
				  Calendar calendar = new GregorianCalendar();
				  
				  shutTheDogFinished.putExtra("seconds", calendar.get(Calendar.SECOND));
				  shutTheDogFinished.putExtra("minutes", calendar.get(Calendar.MINUTE));
				  shutTheDogFinished.putExtra("hours", calendar.get(Calendar.HOUR_OF_DAY));
				  shutTheDogFinished.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
				  shutTheDogFinished.putExtra("month", calendar.get(Calendar.MONTH));
				  shutTheDogFinished.putExtra("year", calendar.get(Calendar.YEAR));
				  shutTheDogFinished.putExtra("dateTimeStart", dateTimeStart.toString());
				  startActivity(shutTheDogFinished);
				  this.finish();				  
			  }
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
