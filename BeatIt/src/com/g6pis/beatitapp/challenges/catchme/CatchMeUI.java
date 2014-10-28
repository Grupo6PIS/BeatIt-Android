package com.g6pis.beatitapp.challenges.catchme;

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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class CatchMeUI extends Activity implements OnClickListener {
		private static final String CHALLENGE_ID = "7";
		
		private CatchMe catchMe;
		private DTState state;
	
		private Button startButton;
//		private ProgressBar progressBar;
//		private CountDownTimer timer;
		private CountDownTimer totalTimer;
		
		private int color;
		private int colorId;
		private int good = 0;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.catch_me);


			catchMe = (CatchMe) Factory.getInstance().getIDataManager().getChallenge(
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

			switch (catchMe.getLevel()) {
			case 1:
				((TextView) findViewById(R.id.textView_Description_Value_2))
						.setText(getResources().getString(
								R.string.description_catch_me_1));
				break;
			case 2:
				((TextView) findViewById(R.id.textView_Description_Value_2))
						.setText(getResources().getString(
								R.string.description_catch_me_2));
				break;
			}
		
			startButton = (Button) findViewById(R.id.start_button);
			startButton.setOnClickListener(this);
//			progressBar = (ProgressBar) findViewById(R.id.progress_bar);
			
			this.editActionBar();
			
//			timer = createTimer();
			
			totalTimer = createTotalTimer();
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
			catchMe.reset();
//			timer.cancel();
			totalTimer.cancel();
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
				catchMe.reset();
//				timer.cancel();
				totalTimer.cancel();
				Intent home = new Intent(this, Home.class);
				startActivity(home);
				this.finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		

		@Override
		public void onClick(View v) {
			
//			Log.i("CatchMe", "Click");
//			Log.i("CatchMe", "Button " + v.getId());
//			Log.i("CatchMe", "ColorId " + colorId);
			if(v.getId() == R.id.start_button){
				
				changeButton();
				((RelativeLayout)findViewById(R.id.before_strat_challenge_layout)).setVisibility(View.INVISIBLE);
				((RelativeLayout)findViewById(R.id.after_strat_challenge_layout)).setVisibility(View.VISIBLE);
				
				startButton.setVisibility(View.INVISIBLE);
//				progressBar.setProgress(catchMe.getTimeSpan());
				
				
//				
//				timer.start();
//				totalTimer.start();
				
				
			}

			else if(v.getId() == colorId){
			
					catchMe.successful();
					changeColorProgress(getResources().getColor(R.color.green));
					good ++;
					totalTimer.cancel();
					changeButton();
					
					
					
			}else{
				
					catchMe.unsuccessful();
					changeColorProgress(getResources().getColor(R.color.red));
					totalTimer.cancel();
					changeButton();
					
					
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
			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(catchMe.getColor())));

		}
		
		public CountDownTimer createTotalTimer(){
			CountDownTimer timer = new CountDownTimer(catchMe.getTime(), catchMe.getTimeSpan()+1) {
				public void onTick(long millisUntilFinished) {
//					int progress = (int)(millisUntilFinished*catchMe.getTimeSpan())/catchMe.getTime();
//					Log.i("CatchMe", "Progress " + progress);
//					progressBar.setProgress(progress);
				}

				public void onFinish() {
					catchMe.unsuccessful();
					changeColorProgress(getResources().getColor(R.color.red));
										
					changeButton();
				}
			};
			
			return timer;
		}
		
	
		public void changeButton(){
		
			
			if(!catchMe.isCompleted()){
				
				Button button;
				button = ((Button) findViewById(colorId));
				
				if (button == null)
					button = ((Button) findViewById(R.id.button1));
				else
					button.setBackgroundResource(R.drawable.round_button);
				
				Random buttonRand = new Random();
				
				color = buttonRand.nextInt(11)+1;
				
				switch(color){
					case 1: 
						button  = ((Button) findViewById(R.id.button1));
						
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 2: 
						button  = ((Button) findViewById(R.id.button2));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 3: 
						button  = ((Button) findViewById(R.id.button3));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 4:
						button  = ((Button) findViewById(R.id.button4));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 5: 
						button  = ((Button) findViewById(R.id.button5));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 6: 
						button  = ((Button) findViewById(R.id.button6));
						button.setBackgroundResource(R.drawable.round_button_green); break;
					
					case 7:
						button  = ((Button) findViewById(R.id.button7));
						button.setBackgroundResource(R.drawable.round_button_green); break;
					
					case 8:
						button  = ((Button) findViewById(R.id.button8));
						button.setBackgroundResource(R.drawable.round_button_green); break;
					
					case 9:
						button  = ((Button) findViewById(R.id.button9));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 10:
						button  = ((Button) findViewById(R.id.button10));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 11:
						button  = ((Button) findViewById(R.id.button11));
						button.setBackgroundResource(R.drawable.round_button_green); break;
						
					case 12:
						button  = ((Button) findViewById(R.id.button12));
						button.setBackgroundResource(R.drawable.round_button_green); break;
				
				}
				
				colorId = button.getId();
				
				totalTimer.start();
			
			}else{
//				Log.i("CatchMe", "Termino");
				catchMe.finishChallenge(good);
				Intent challengeFinished = new Intent(this, CatchMeFinished.class);
				startActivity(challengeFinished);
				this.finish();
				StateDAO db = new StateDAO(this);
				db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
				
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
			switch(catchMe.getCurrentCount()){
			case 1:
				((TextView)findViewById(R.id.TextView1)).setBackgroundColor(color);
				break;
				
			case 2:
			case 17:
			case 32:
			case 47:
			case 62:
			case 77:
				((TextView)findViewById(R.id.TextView2)).setBackgroundColor(color);
				break;
			case 3:
			case 18:
			case 33:
			case 48:
			case 63:
			case 78:
				((TextView)findViewById(R.id.TextView3)).setBackgroundColor(color);
				break;
			case 4:
			case 19:
			case 34:
			case 49:
			case 64:
			case 79:
				((TextView)findViewById(R.id.TextView4)).setBackgroundColor(color);
				break;
			case 5:
			case 20:
			case 35:
			case 50:
			case 65:
			case 80:
				((TextView)findViewById(R.id.TextView5)).setBackgroundColor(color);
				break;
			case 6:
			case 21:
			case 36:
			case 51:
			case 66:
			case 81:
				((TextView)findViewById(R.id.TextView6)).setBackgroundColor(color);
				break;
			case 7:
			case 22:
			case 37:
			case 52:
			case 67:
			case 82:
				((TextView)findViewById(R.id.TextView7)).setBackgroundColor(color);
				break;
			case 8:
			case 23:
			case 38:
			case 53:
			case 68:
			case 83:
				((TextView)findViewById(R.id.TextView8)).setBackgroundColor(color);
				break;
			case 9:
			case 24:
			case 39:
			case 54:
			case 69:
			case 84:
				((TextView)findViewById(R.id.TextView9)).setBackgroundColor(color);
				break;
			case 10:
			case 25:
			case 40:
			case 55:
			case 70:
			case 85:
				((TextView)findViewById(R.id.TextView10)).setBackgroundColor(color);
				break;
			case 11:
			case 26:
			case 41:
			case 56:
			case 71:
			case 86:
				((TextView)findViewById(R.id.TextView11)).setBackgroundColor(color);
				break;
			case 12:
			case 27:
			case 42:
			case 57:
			case 72:
			case 87:
				((TextView)findViewById(R.id.TextView12)).setBackgroundColor(color);
				break;
			case 13:
			case 28:
			case 43:
			case 58:
			case 73:
			case 88:
				((TextView)findViewById(R.id.TextView13)).setBackgroundColor(color);
				break;
			case 14:
			case 29:
			case 44:
			case 59:
			case 74:
			case 89:
				((TextView)findViewById(R.id.TextView14)).setBackgroundColor(color);
				break;
			case 15:
			case 30:
			case 45:
			case 60:
			case 75:
			case 90:
				((TextView)findViewById(R.id.TextView15)).setBackgroundColor(color);
				break;
			case 16:
			case 31:
			case 46:
			case 61:
			case 76:
				resetProgress();
				((TextView)findViewById(R.id.TextView1)).setBackgroundColor(color);
				break;
			}
		
		}
		
		@SuppressWarnings("deprecation")
		public void resetProgress(){
			((TextView)findViewById(R.id.TextView1)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView2)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView3)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView4)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView5)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView6)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView7)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView8)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView9)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView10)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView11)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView12)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView13)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView14)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
			((TextView)findViewById(R.id.TextView15)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		}
		
}
