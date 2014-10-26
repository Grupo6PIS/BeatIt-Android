package com.g6pis.beatitapp.challenges.songcomplete;

import java.io.IOException;
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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SongCompleteUI extends Activity {
	private static final String CHALLENGE_ID = "9";
	private SongComplete songcomplete;
	private DTState state;
	
	private MediaPlayer mp;
	private Handler handler = new Handler();
	
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	
	CountDownTimer c;
	
	int number_of_guessed = 0;

	//Rangos nivel 1
	String allArtists[][] = {
			{ "Red Hot Chili Peppers", "Led Zeppelin", "Foo Fighters" },
			{ "Pitbull", "Flo Rida", "LMFAO" },
			{ "David Guetta", "Calvin Harris", "Avicii" },
			{ "Bruno Mars", "Justin Timberlake", "Maroon 5" },
			{ "Usher", "Pharrell Williams", "Kanye West" },
	};
	
	String allSongs[][] = {
			{ "Paradise City", "November Rain", "Sweet Child O' Mine" },
			{ "Satisfaction", "Angie", "Start Me Up" },
			{ "One More Night", "Animals", "She Will Be Loved" },
			{ "Hey Jude", "Yesterday", "Let It Be" },
			{ "It's My Life", "Crazy", "Always" },
	};
	
	String correctArtists[] = {"Red Hot Chili Peppers", "Flo Rida", "Avicii", "Maroon 5", "Pharrell Williams"};
	String correctSongs[] = {"Sweet Child O' Mine", "Angie", "One More Night", "Let It Be", "It's My Life"};
	
	String datos[][]; //auxiliar
	
	//Rangos nivel 2
	int correctCounter = 0;
	private int secondCount;
	private int songCount = 5;
	private int roundNumber;
	private int currentSong;
	private int pressedButton;
	private int maxDelay; //en segundos
	private boolean counterRunning;
	private ProgressBar bar;

	int it = 0;
	boolean firstTime = true;
		
	boolean hasWon = true;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_complete);
		
		songcomplete = (SongComplete) DataManager.getInstance().getChallenge(
				CHALLENGE_ID);
		state = DataManager.getInstance().getState(CHALLENGE_ID);
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
		
		//todo: arreglar el tema de la descripcion
		/*switch(songcomplete.getLevel()){
			case 1: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_shut_the_dog_1));
			case 2: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_shut_the_dog_2));
		}*/
		
		if (songcomplete.getLevel() == 1){
			datos = allArtists;
		} else {
			datos = allSongs;
		}

		this.editActionBar();
		
		counterRunning = false;
		if (songcomplete.getLevel() == 1){
			maxDelay = 10;
		} else {
			maxDelay = 20;
		}
		
		currentSong = 0;
		
		//cargo el progessbar
		bar = (ProgressBar)findViewById(R.id.progressBar_Song_Complete);
		bar.setIndeterminate(false); // May not be necessary
		bar.setMax(maxDelay*1000);
		
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
	
	public void button1Click(View v){
		if (!counterRunning){
			
			Button b = (Button)findViewById(R.id.btnOpcion1);
			b.setText(getResources().getString(R.string.song_complete_started));
			
			Button b1 = (Button)findViewById(R.id.btnOpcion2);
			b1.setVisibility(0);
			
			Button b2 = (Button)findViewById(R.id.btnOpcion3);
			b2.setVisibility(0);
			
			roundNumber = 1;
			pressedButton = 0;
			handler.postDelayed(rutine, 3000);
		} else {
			pressedButton = 0;
			this.buttonPressed();
		}
	}
	
	public void button2Click(View v){
		pressedButton = 1;
		this.buttonPressed();
	}
	
	public void button3Click(View v){
		pressedButton = 2;
		this.buttonPressed();
	}
	
	private void buttonPressed(){
		secondCount = 0;
		counterRunning = false;
		c.cancel();
		mp.stop();
		
		LinearLayout l = null;
		switch(roundNumber){
			case 1: l= (LinearLayout)findViewById(R.id.bar1);
					break;
			case 2: l= (LinearLayout)findViewById(R.id.bar2);
					break;
			case 3: l= (LinearLayout)findViewById(R.id.bar3);
					break;
			case 4: l= (LinearLayout)findViewById(R.id.bar4);
					break;
			case 5: l= (LinearLayout)findViewById(R.id.bar5);
					break;
		}
		
		if (checkResult()){
			//Acerto
			number_of_guessed ++;
			l.setBackgroundColor(getResources().getColor(R.color.verde));
		} else {
			//No acerto
			l.setBackgroundColor(getResources().getColor(R.color.red));
		}
		
		if (roundNumber == 5){
			//Termino la ronda
			roundNumber = 0;
			songcomplete.setSucceed_times(number_of_guessed);
			number_of_guessed = 0;
			this.completeChallenge();	
		} else {
			if (currentSong != 5){
				currentSong++;				
			}
			//Sigue la ronda
			roundNumber++;
			handler.postDelayed(rutine, 2000);
		}
	}
	
	private Runnable rutine = new Runnable() {
		@Override
		public void run() {
			Button b = (Button)findViewById(R.id.btnOpcion1);
			b.setText(datos[currentSong][0]);
			
			Button b1 = (Button)findViewById(R.id.btnOpcion2);
			b1.setText(datos[currentSong][1]);
			
			Button b2 = (Button)findViewById(R.id.btnOpcion3);
			b2.setText(datos[currentSong][2]);
			
			if (songcomplete.getLevel() == 1){
				switch(currentSong){
					case 0: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song1level1);
							break;
					case 1: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song2level1);
							break;
					case 2: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song3level1);
							break;
					case 3: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song4level1);
							break;
					case 4: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song5level1);
							break;
				}
			} else {
				switch(currentSong){
					case 0: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song1level2);
							break;
					case 1: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song2level2);
							break;
					case 2: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song3level2);
							break;
					case 3: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song4level2);
							break;
					case 4: mp = MediaPlayer.create(SongCompleteUI.this, R.raw.song5level2);
							break;
				}
			}
			mp.setLooping(true);
			
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
						
			c = new CountDownTimer(maxDelay*1000, 1) {
				
				public void onTick(long millisUntilFinished) {
					secondCount++;
					counterRunning = true;
					
					//Cargo la progressbar
					int mils = (int) millisUntilFinished;
					bar.setProgress((maxDelay*1000) - mils);
				}
				
				public void onFinish() {
					//PERDIO					
					secondCount = 0;
					counterRunning = false;
					c.cancel();
					mp.stop();
					
					LinearLayout l = null;
					switch(roundNumber){
						case 1: l= (LinearLayout)findViewById(R.id.bar1);
								break;
						case 2: l= (LinearLayout)findViewById(R.id.bar2);
								break;
						case 3: l= (LinearLayout)findViewById(R.id.bar3);
								break;
						case 4: l= (LinearLayout)findViewById(R.id.bar4);
								break;
						case 5: l= (LinearLayout)findViewById(R.id.bar5);
								break;
					}
					
					if (checkResult()){
						//Acerto
						number_of_guessed ++;
						l.setBackgroundColor(getResources().getColor(R.color.verde));
					} else {
						//No acerto
						l.setBackgroundColor(getResources().getColor(R.color.red));
					}
					
					if (currentSong != 5){
						currentSong++;				
					}
					
					if (roundNumber == 5){
						//Termino la ronda
						roundNumber = 0;
						songcomplete.setSucceed_times(number_of_guessed);
						number_of_guessed = 0;
						SongCompleteUI.this.completeChallenge();	
					} else {
						//Sigue la ronda
						roundNumber++;
						handler.postDelayed(rutine, 2000);
					}
				}
				
			}.start();
		}
	};

	protected void onResume() {
	  super.onResume();
	}
	
	private boolean checkResult(){
		boolean ret = false;
		
		if(songcomplete.getLevel() == 1){
			if(allArtists[currentSong][pressedButton].equals(correctArtists[currentSong]))
				ret = true;
		} else {
			if(allSongs[currentSong][pressedButton].equals(correctSongs[currentSong]))
				ret = true;
		}
		
		return ret;
	}
	 
	// Customize ActionBar
	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.song_complete)));
	}
		
	 protected void onPause() {
	  super.onPause();
	 }

	 public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 }
	 
	 public void onPrepared(MediaPlayer player) {
		 player.start();
	 }
	 
	 public void completeChallenge() {
		Intent finished = new Intent(this, SongCompleteFinished.class);
		startActivity(finished);
		this.finish();
		songcomplete.finishChallenge();
		StateDAO db = new StateDAO(this);
		db.updateState(DataManager.getInstance().getState(CHALLENGE_ID));
	}
	 
	 @Override
		public void onBackPressed() {
		 	secondCount = 0;
		 	if (counterRunning){
		 		c.cancel();
		 	}
			counterRunning = false;
			if(mp !=null){
				if (mp.isPlaying()){
					mp.stop();				
				}
			}
			roundNumber = 0;
			songcomplete.setSucceed_times(number_of_guessed);
			number_of_guessed = 0;
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
				secondCount = 0;
				if (counterRunning){
			 		c.cancel();
			 	}
				counterRunning = false;
				if(mp !=null){
					if (mp.isPlaying()){
						mp.stop();				
					}
				}
				roundNumber = 0;
				songcomplete.setSucceed_times(number_of_guessed);
				number_of_guessed = 0;
				Intent home = new Intent(this, Home.class);
				startActivity(home);
				this.finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		
		public String getDurationString(){
			String result = "";
			DTDateTime now = new DTDateTime();
			
			String difference = state.getDateTimeFinish().diff(now);
			Integer diff = state.getDateTimeFinish().diff(now, difference);
			
			if(difference.equals("year"))
				if(diff > 1)
					result = diff + " " + getResources().getString(R.string.years);
				else
					result = diff + " " + getResources().getString(R.string.year);
			
			if(difference.equals("month"))
				if(diff > 1)
					result = diff + " " + getResources().getString(R.string.months);
				else
					result = diff + " " + getResources().getString(R.string.month);

			if(difference.equals("day"))
				if(diff > 1){
					if(diff > 6){
						if(diff > 13){
							diff = (int) Math.ceil(diff/7);
							result = diff + " " + getResources().getString(R.string.weeks);
						}else{
							diff = (int) Math.ceil(diff/7);
							result = diff + " " + getResources().getString(R.string.week);
						}
							
					}else
						result = diff + " " + getResources().getString(R.string.days);
				}else
					result = diff + " " + getResources().getString(R.string.day);

			if(difference.equals("hour"))
				if(diff > 1)
					result = diff + " " + getResources().getString(R.string.hours);
				else
					result = diff + " " + getResources().getString(R.string.hour);
			
			if(difference.equals("minute"))
				if(diff > 1)
					result = diff + " " + getResources().getString(R.string.minutes);
				else
					result = diff + " " + getResources().getString(R.string.minute);
			
			if(difference.equals("second"))
				if(diff > 1)
					result = diff + " " + getResources().getString(R.string.seconds);
				else
					result = diff + " " + getResources().getString(R.string.second);
			
			return result;
	        
	    }
}
