package com.g6pis.beatitapp.challenges.songcomplete;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		songcomplete = (SongComplete) Factory.getInstance().getIDataManager().getChallenge(
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
		
		//todo: arreglar el tema de la descripcion
		switch(songcomplete.getLevel()){
			case 1: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_song_complete_1));
			case 2: ((TextView)findViewById(R.id.textView_Description_Value_2)).setText(getResources().getString(R.string.description_song_complete_2));
		}
		
		if (songcomplete.getLevel() == 1){
			datos = allArtists;
		} else {
			datos = allSongs;
		}

		this.editActionBar();
		
		counterRunning = false;
		if (songcomplete.getLevel() == 1){
			maxDelay = 20;
		} else {
			maxDelay = 10;
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
			
			((RelativeLayout)findViewById(R.id.progress_layout)).setVisibility(View.VISIBLE);
			((RelativeLayout)findViewById(R.id.info_layout)).setVisibility(View.INVISIBLE);
			((ProgressBar)findViewById(R.id.progressBar_Song_Complete)).getProgressDrawable().setColorFilter(Color.parseColor(songcomplete.getColor()),Mode.SRC_IN);
			
			b.setEnabled(false);
			b1.setEnabled(false);
			b2.setEnabled(false);
			
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
		
		Button b = (Button)findViewById(R.id.btnOpcion1);
		b.setEnabled(false);
		
		Button b1 = (Button)findViewById(R.id.btnOpcion2);
		b1.setEnabled(false);
		
		Button b2 = (Button)findViewById(R.id.btnOpcion3);
		b2.setEnabled(false);
		
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
			
			b.setEnabled(true);
			b1.setEnabled(true);
			b2.setEnabled(true);
			
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
					
					Button b = (Button)findViewById(R.id.btnOpcion1);
					b.setEnabled(false);
					
					Button b1 = (Button)findViewById(R.id.btnOpcion2);
					b1.setEnabled(false);
					
					Button b2 = (Button)findViewById(R.id.btnOpcion3);
					b2.setEnabled(false);
					
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
		 songcomplete.finishChallenge();
		 if (Factory.getInstance().getIDataManager().getHaveToSendScore()) {
				Thread t = new Thread() {
					public void run() {

						Factory.getInstance().getIDataManager().sendScore();
						Factory.getInstance().getIDataManager().updateRanking();
					}
				};

				t.start();
			}
		 Intent finished = new Intent(this, SongCompleteFinished.class);
		startActivity(finished);
		this.finish();
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
