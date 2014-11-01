package com.g6pis.beatitapp.challenges.shutthedog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class ShutTheDogUI extends Activity implements SensorEventListener {
	private static final String CHALLENGE_ID = "4";
	private ShutTheDog shutthedog;
	private DTState state;
	
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
		
		shutthedog = (ShutTheDog) Factory.getInstance().getIDataManager().getChallenge(
				CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value))
		.setText(this.getDurationString());
		
		if(state.getMaxScore() > 0)
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setText(Double.toString(state.getMaxScore()));
		else{
			((TextView)findViewById(R.id.textView_To_Beat_Value)).setVisibility(View.INVISIBLE);
			((TextView)findViewById(R.id.textView_To_Beat)).setVisibility(View.INVISIBLE);
		}
		
		switch(shutthedog.getLevel()){
		case 1: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_shut_the_dog_1));
		case 2: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_shut_the_dog_2));
		}
		
		for (int j = 0; j<5;j++){
			 results[j] = 0;
		}
		

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
		Button b = (Button)findViewById(R.id.btnOpcion1);
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
					} catch (IOException e) {
					}
				}
			}
			
			mSensorManager.registerListener(ShutTheDogUI.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);	
			
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
					
					shutthedog.setHasWon(true);
					shutthedog.setResults(results);
					ShutTheDogUI.this.completeChallenge();
				}
				
			}.start();
		}
	};

	 protected void onResume() {
	  super.onResume();
	 }
	 
	// Customize ActionBar
	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
	  if (event.values[0] <= 3) {
		  if (mp != null){
			  mSensorManager.unregisterListener(ShutTheDogUI.this, mSensor);
			  mp.stop();
			  c.cancel();
			  
			  Button b = (Button)findViewById(R.id.btnOpcion1);
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
			db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
		}
	 
	 @Override
		public void onBackPressed() {
		 	if(mp !=null){
				if (mp.isPlaying()){
					mp.stop();				
				}
			}
		 	
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
				if(mp !=null){
					if (mp.isPlaying()){
						mp.stop();				
					}
				}
				
				Intent home = new Intent(this, Home.class);
				startActivity(home);
				this.finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
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
}
