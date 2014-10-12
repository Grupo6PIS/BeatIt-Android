package com.g6pis.beatitapp.challenges.shutthedog;

import java.io.IOException;
import java.util.Random;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.persistence.StateDAO;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShutTheDogUI extends Activity implements SensorEventListener {
	private static final String CHALLENGE_ID = "4";
	private ShutTheDog shutthedog;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private MediaPlayer mp;
	private Handler handler = new Handler();
	
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	
	CountDownTimer c;
	
	int number_of_barks = 0;
	int rangeMin[];
	int rangeMax[];
	
	//Rangos nivel 1
	int minRange1[] = {2,2,2};
	int maxRange1[] = {4,4,4};
	
	//Rangos nivel 2
	int minRange2[] = {2,2,2,2,2};
	int maxRange2[] = {3,3,3,3,3};
	
	//Array de resultados
	int results[] = new int[5];
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
		
		shutthedog = (ShutTheDog) DataManager.getInstance().getChallenge(
				CHALLENGE_ID);
		DTState state = DataManager.getInstance().getState(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value))
				.setText(state.getDateTimeFinish().toString());
		

		this.editActionBar();
		
		if (shutthedog.getLevel() == 1){
			number_of_barks = 3;
			rangeMin = minRange1;
			rangeMax = maxRange1;
		} else {
			number_of_barks = 5;
			rangeMin = minRange2;
			rangeMax = maxRange2;
		}
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
		int segundosComienzo = r.nextInt(rangeMax[it] - rangeMin[it] + 1) + rangeMin[it];
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
			  
			  if (it <= (number_of_barks-1)){
				it++;
				if (it !=number_of_barks){
					Random r = new Random();
					int segundosComienzo = r.nextInt(rangeMax[it] - rangeMin[it] + 1) + rangeMin[it];
					int tiempo = segundosComienzo * 1000;
					handler.postDelayed(rutine, tiempo);					
				}
			  }
			  
			  if(it == number_of_barks){
				  //GANO EL DESAFIO
				  //Calculo el puntaje
				  shutthedog.setHasWon(true);
				  shutthedog.setResults(results);
				  this.completeChallenge();
			  }
		  }
	  } else {
		  //Esta lejos
	  }
	 }
	 
	 public void completeChallenge() {
			Intent finished = new Intent(this, ShutTheDogFinished.class);
			startActivity(finished);
			this.finish();
			shutthedog.finishChallenge();
			StateDAO db = new StateDAO(this);
			db.updateState(DataManager.getInstance().getState(CHALLENGE_ID));
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
