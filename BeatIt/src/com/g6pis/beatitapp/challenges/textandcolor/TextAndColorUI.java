package com.g6pis.beatitapp.challenges.textandcolor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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


public class TextAndColorUI extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "8";
	
	private TextAndColor textAndColor;
	private DTState state;
	
	private ImageButton yesButton;
	private ImageButton noButton;
	private Button startButton;
	private ProgressBar progressBar;
	private CountDownTimer timer;
	
	private int color;
	private int word;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_and_color);


		textAndColor = (TextAndColor) Factory.getInstance().getIDataManager().getChallenge(
				CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value))
				.setText(this.getDurationString());
		
		if (state.getMaxScore() > 0)
			((TextView) findViewById(R.id.textView_To_Beat_Value))
					.setText(Double.toString(state.getMaxScore()));
		else {
			((TextView) findViewById(R.id.textView_To_Beat_Value))
					.setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.textView_To_Beat))
					.setVisibility(View.INVISIBLE);

		}

		switch (textAndColor.getLevel()) {
		case 1:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.description_text_and_color_1));
			break;
		case 2:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.description_text_and_color_2));
			break;
		}
		
		yesButton = (ImageButton) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (ImageButton) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		
		this.editActionBar();
		
		timer = createTimer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		textAndColor.reset();
		timer.cancel();
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
			textAndColor.reset();
			timer.cancel();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_button:{
			changeWord();
			((RelativeLayout)findViewById(R.id.before_strat_challenge_layout)).setVisibility(View.INVISIBLE);
			((RelativeLayout)findViewById(R.id.after_strat_challenge_layout)).setVisibility(View.VISIBLE);
			yesButton.setVisibility(View.VISIBLE);
			noButton.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.INVISIBLE);
			progressBar.setProgress(100);
			timer.start();
			
		}
			break;
		case R.id.yes_button:{
			if(color==word){
				textAndColor.addCount();;
				changeColorProgress();
				changeWord();
			}else{
				completeChallenge();
			}

		}
			break;
		case R.id.no_button:{
			if(color!=word){
				textAndColor.addCount();
				changeColorProgress();
				changeWord();
			}else{
				completeChallenge();
			}

		}
			break;
		}

	}

	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(textAndColor.getColor())));

	}
	
	public CountDownTimer createTimer(){
		CountDownTimer timer = new CountDownTimer(textAndColor.getTime(), 10l) {
			public void onTick(long millisUntilFinished) {
				int progress = (int)(millisUntilFinished*100)/textAndColor.getTime();
				progressBar.setProgress(progress);
				
			}

			public void onFinish() {
				completeChallenge();
			}
		};
		
		return timer;
	}
	
	public void changeWord(){
			TextView text = ((TextView) findViewById(R.id.textView_text));
			Random colorRand = new Random();
			color = colorRand.nextInt(6)+1;
			switch(color){
				case 1:{
					text.setTextColor(getResources().getColor(R.color.violet));
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.violet),Mode.SRC_IN);

				}
				break;
				case 2:{
					text.setTextColor(getResources().getColor(R.color.red));
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red),Mode.SRC_IN);
				}
				break;
				case 3:{
					text.setTextColor(getResources().getColor(R.color.green));
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.green),Mode.SRC_IN);
				}
				break;
				case 4:{
					text.setTextColor(getResources().getColor(R.color.blue));
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.blue),Mode.SRC_IN);
				}
				break;
				case 5:{
					text.setTextColor(getResources().getColor(R.color.yellow));		
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.yellow),Mode.SRC_IN);
				}
				break;
				case 6:{
					text.setTextColor(getResources().getColor(R.color.orange));	
					progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange),Mode.SRC_IN);
				}
				break;
				
				
			}
				
			Random wordRand = new Random();
			word = wordRand.nextInt(6)+1;
			switch(word){
				case 1: text.setText(getResources().getString(R.string.violet));break;
				case 2: text.setText(getResources().getString(R.string.red));break;
				case 3: text.setText(getResources().getString(R.string.green));break;
				case 4: text.setText(getResources().getString(R.string.blue));break;
				case 5: text.setText(getResources().getString(R.string.yellow));break;
				case 6: text.setText(getResources().getString(R.string.orange));break;
			
			}
		
	}
	
public String getDurationString(){
		
		double finishSeconds = state.getFinishSeconds();
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT")); 
		Date date = cal.getTime();
		double currentSeconds = date.getTime()/1000;

		double duration = (finishSeconds - currentSeconds);
		String result = "";
		int d = ((int)duration);
		if(duration/60 > 0){
			duration = Math.ceil(duration/60);
			if(duration/60 > 0){
				duration = Math.ceil(duration/60);
				if(duration/24 > 0){
					duration = Math.ceil(duration/24);
					if(duration/7 > 0){
						duration = Math.ceil(duration/7);
						if(duration > 1)
							result = ((int)duration) + getResources().getString(R.string.weeks);
						else
							result = ((int)duration) + getResources().getString(R.string.week);
					}else{
						if(duration > 1)
							result = ((int)duration) + getResources().getString(R.string.days);
						else
							result = ((int)duration) + getResources().getString(R.string.day);
					}
				}else{
					if(duration > 1)
						result = ((int)duration) + getResources().getString(R.string.hours);
					else
						result = ((int)duration) + getResources().getString(R.string.hour);
				}
			}else{
				if(duration > 1)
					result = ((int)duration) + getResources().getString(R.string.minutes);
				else
					result = ((int)duration) + getResources().getString(R.string.minute);
			}
		}else{
				result = getResources().getString(R.string.few_seconds);
		}
		return result;
    }
	
	public void changeColorProgress(){
		switch(textAndColor.getCount()){
		case 1:
			((LinearLayout)findViewById(R.id.bar1)).setBackgroundColor(getResources().getColor(R.color.red));
			break;			
		case 2:
			((LinearLayout)findViewById(R.id.bar2)).setBackgroundColor(getResources().getColor(R.color.red));
			break;
		case 3:
			((LinearLayout)findViewById(R.id.bar3)).setBackgroundColor(getResources().getColor(R.color.red));
			break;
		case 4:
			((LinearLayout)findViewById(R.id.bar4)).setBackgroundColor(getResources().getColor(R.color.red));
			break;
		case 5:
			((LinearLayout)findViewById(R.id.bar5)).setBackgroundColor(getResources().getColor(R.color.red));
			break;
		case 6:{
			resetProgress();
			((LinearLayout)findViewById(R.id.bar1)).setBackgroundColor(getResources().getColor(R.color.yellow));
			((TextView)findViewById(R.id.progress_number)).setTextColor(getResources().getColor(R.color.yellow));
		}
			break;
		case 7:
			((LinearLayout)findViewById(R.id.bar2)).setBackgroundColor(getResources().getColor(R.color.yellow));
			break;
		case 8:
			((LinearLayout)findViewById(R.id.bar3)).setBackgroundColor(getResources().getColor(R.color.yellow));
			break;
		case 9:
			((LinearLayout)findViewById(R.id.bar4)).setBackgroundColor(getResources().getColor(R.color.yellow));
			break;
		case 10:
			((LinearLayout)findViewById(R.id.bar5)).setBackgroundColor(getResources().getColor(R.color.yellow));
			break;
		case 11:{
			resetProgress();
			((LinearLayout)findViewById(R.id.bar1)).setBackgroundColor(getResources().getColor(R.color.green));
			((TextView)findViewById(R.id.progress_number)).setTextColor(getResources().getColor(R.color.green));
		}
			break;
		case 12:
			((LinearLayout)findViewById(R.id.bar2)).setBackgroundColor(getResources().getColor(R.color.green));
			break;
		case 13:
			((LinearLayout)findViewById(R.id.bar3)).setBackgroundColor(getResources().getColor(R.color.green));
			break;
		case 14:
			((LinearLayout)findViewById(R.id.bar4)).setBackgroundColor(getResources().getColor(R.color.green));
			break;
		case 15:
			((LinearLayout)findViewById(R.id.bar5)).setBackgroundColor(getResources().getColor(R.color.green));
			break;
		}
		
		((TextView)findViewById(R.id.progress_number)).setText(Integer.toString(textAndColor.getCount()));
		
	}
	
	public void completeChallenge(){
		timer.cancel();
		textAndColor.finishChallenge();
		Intent challengeFinished = new Intent(this, TextAndColorFinished.class);
		startActivity(challengeFinished);
		this.finish();
		StateDAO db = new StateDAO(this);
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
	}
	
	@SuppressWarnings("deprecation")
	public void resetProgress(){
		((LinearLayout)findViewById(R.id.bar1)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		((LinearLayout)findViewById(R.id.bar2)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		((LinearLayout)findViewById(R.id.bar3)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		((LinearLayout)findViewById(R.id.bar4)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		((LinearLayout)findViewById(R.id.bar5)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
	}
	
}
