package com.g6pis.beatitapp.challenges.textandcolor;

import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.persistence.StateDAO;


public class TextAndColorUI extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "8";
	
	private TextAndColor textAndColor;
	private DTState state;
	
	private Button yesButton;
	private Button noButton;
	private Button startButton;
	private ProgressBar progressBar;
	private CountDownTimer timer;
	
	private int color;
	private int word;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_and_color);


		textAndColor = (TextAndColor) DataManager.getInstance().getChallenge(
				CHALLENGE_ID);
		state = DataManager.getInstance().getState(CHALLENGE_ID);
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
			((LinearLayout)findViewById(R.id.progress4_layout)).setVisibility(View.INVISIBLE);
			((LinearLayout)findViewById(R.id.progress5_layout)).setVisibility(View.INVISIBLE);
			((LinearLayout)findViewById(R.id.progress6_layout)).setVisibility(View.INVISIBLE);
			break;
		case 2:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.description_text_and_color_2));
			break;
		}
		
		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
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
			
		}
			break;
		case R.id.yes_button:{
			if(color==word){
				textAndColor.successful();
				changeColorProgress(getResources().getColor(R.color.green));
			}else{
				textAndColor.unsuccessful();
				changeColorProgress(getResources().getColor(R.color.red));
			}
			
			timer.cancel();
			changeWord();
			
		}
			break;
		case R.id.no_button:{
			if(color!=word){
				textAndColor.successful();
				changeColorProgress(getResources().getColor(R.color.green));
			}else{
				textAndColor.unsuccessful();
				changeColorProgress(getResources().getColor(R.color.red));
			}
			
			timer.cancel();
			changeWord();
		}
			break;
		}

	}

	public void editActionBar() {
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
				textAndColor.unsuccessful();
				changeColorProgress(getResources().getColor(R.color.red));
				changeWord();
			}
		};
		
		return timer;
	}
	
	public void changeWord(){
		if(!textAndColor.isCompleted()){
			TextView text = ((TextView) findViewById(R.id.textView_text));
			Random colorRand = new Random();
			color = colorRand.nextInt(6)+1;
			switch(color){
				case 1: text.setTextColor(getResources().getColor(R.color.violet));break;
				case 2: text.setTextColor(getResources().getColor(R.color.red));break;
				case 3: text.setTextColor(getResources().getColor(R.color.green));break;
				case 4: text.setTextColor(getResources().getColor(R.color.blue));break;
				case 5: text.setTextColor(getResources().getColor(R.color.yellow));break;
				case 6: text.setTextColor(getResources().getColor(R.color.orange));break;
				
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
			
			timer.start();
		}else{
			
			textAndColor.finishChallenge();
			Intent challengeFinished = new Intent(this, TextAndColorFinished.class);
			startActivity(challengeFinished);
			this.finish();
			StateDAO db = new StateDAO(this);
			db.updateState(DataManager.getInstance().getState(CHALLENGE_ID));
			
		}
		
	
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
	
	public void changeColorProgress(int color){
		switch(textAndColor.getCurrentCount()){
		case 1:
			((TextView)findViewById(R.id.TextView1)).setBackgroundColor(color);
			break;
			
		case 2:
			((TextView)findViewById(R.id.TextView2)).setBackgroundColor(color);
			break;
		case 3:
			((TextView)findViewById(R.id.TextView3)).setBackgroundColor(color);
			break;
		case 4:
			((TextView)findViewById(R.id.TextView4)).setBackgroundColor(color);
			break;
		case 5:
			((TextView)findViewById(R.id.TextView5)).setBackgroundColor(color);
			break;
		case 6:
			((TextView)findViewById(R.id.TextView6)).setBackgroundColor(color);
			break;
		case 7:
			((TextView)findViewById(R.id.TextView7)).setBackgroundColor(color);
			break;
		case 8:
			((TextView)findViewById(R.id.TextView8)).setBackgroundColor(color);
			break;
		case 9:
			((TextView)findViewById(R.id.TextView9)).setBackgroundColor(color);
			break;
		case 10:
			((TextView)findViewById(R.id.TextView10)).setBackgroundColor(color);
			break;
		case 11:
			((TextView)findViewById(R.id.TextView11)).setBackgroundColor(color);
			break;
		case 12:
			((TextView)findViewById(R.id.TextView12)).setBackgroundColor(color);
			break;
		case 13:
			((TextView)findViewById(R.id.TextView13)).setBackgroundColor(color);
			break;
		case 14:
			((TextView)findViewById(R.id.TextView14)).setBackgroundColor(color);
			break;
		case 15:
			((TextView)findViewById(R.id.TextView15)).setBackgroundColor(color);
			break;
		case 16:
			((TextView)findViewById(R.id.TextView16)).setBackgroundColor(color);
			break;
		case 17:
			((TextView)findViewById(R.id.TextView17)).setBackgroundColor(color);
			break;
		case 18:
			((TextView)findViewById(R.id.TextView18)).setBackgroundColor(color);
			break;
		case 19:
			((TextView)findViewById(R.id.TextView19)).setBackgroundColor(color);
			break;
		case 20:
			((TextView)findViewById(R.id.TextView20)).setBackgroundColor(color);
			break;
		case 21:
			((TextView)findViewById(R.id.TextView21)).setBackgroundColor(color);
			break;
		case 22:
			((TextView)findViewById(R.id.TextView22)).setBackgroundColor(color);
			break;
		case 23:
			((TextView)findViewById(R.id.TextView23)).setBackgroundColor(color);
			break;
		case 24:
			((TextView)findViewById(R.id.TextView24)).setBackgroundColor(color);
			break;
		case 25:
			((TextView)findViewById(R.id.TextView25)).setBackgroundColor(color);
			break;
		case 26:
			((TextView)findViewById(R.id.TextView26)).setBackgroundColor(color);
			break;
		case 27:
			((TextView)findViewById(R.id.TextView27)).setBackgroundColor(color);
			break;
		case 28:
			((TextView)findViewById(R.id.TextView28)).setBackgroundColor(color);
			break;
		case 29:
			((TextView)findViewById(R.id.TextView29)).setBackgroundColor(color);
			break;
		case 30:
			((TextView)findViewById(R.id.TextView30)).setBackgroundColor(color);
			break;
		}
	}
	
}
