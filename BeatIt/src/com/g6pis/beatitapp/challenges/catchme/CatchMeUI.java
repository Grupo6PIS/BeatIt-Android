package com.g6pis.beatitapp.challenges.catchme;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	// private ProgressBar progressBar;
	 private CountDownTimer timer;
	private CountDownTimer totalTimer;

	private int color;
	private int colorId;
	// private int good = 0;
	private int times = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catch_me);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		catchMe = (CatchMe) Factory.getInstance().getIDataManager()
				.getChallenge(CHALLENGE_ID);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		((TextView) findViewById(R.id.textView_Duration_Value)).setText(this
				.getDurationString());

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
		// progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		this.editActionBar();

		timer = createTimer();

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
		timer.cancel();
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
			 timer.cancel();
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

		// Log.i("CatchMe", "Click");
		// Log.i("CatchMe", "Button " + v.getId());
		// Log.i("CatchMe", "ColorId " + colorId);
		if (v.getId() == R.id.start_button) {

			timer.start();
//			changeButton();
			((RelativeLayout) findViewById(R.id.before_strat_challenge_layout))
					.setVisibility(View.INVISIBLE);
			((RelativeLayout) findViewById(R.id.after_strat_challenge_layout))
					.setVisibility(View.VISIBLE);

			startButton.setVisibility(View.INVISIBLE);
			// progressBar.setProgress(catchMe.getTimeSpan());

			//
			// timer.start();
			totalTimer.start();

		}

		else if (v.getId() == colorId) {

			catchMe.successful();
			times++;
			changeColorProgress();
			timer.start();
			// good++;
			// totalTimer.cancel();
			// changeButton();

		} else {

			catchMe.unsuccessful();
//			changeColorProgress();
			timer.cancel();
			totalTimer.cancel();
			changeButton();

		}

	}

	public void editActionBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(catchMe.getColor())));

	}

	public CountDownTimer createTotalTimer() {
		CountDownTimer timer = new CountDownTimer(catchMe.getTime(),
				catchMe.getTimeSpan() + 1) {
			public void onTick(long millisUntilFinished) {
				// times ++;

				//				changeButton();
				// int progress =
				// (int)(millisUntilFinished*catchMe.getTimeSpan())/catchMe.getTime();
				// Log.i("CatchMe", "Progress " + progress);
				// progressBar.setProgress(progress);
			}

			public void onFinish() {
				catchMe.unsuccessful();
//				changeColorProgress();

				changeButton();
			}
		};

		return timer;
	}
	
	public CountDownTimer createTimer() {
		CountDownTimer timer = new CountDownTimer(catchMe.getTimeSpan(),
				catchMe.getTimeSpan()) {
			public void onTick(long millisUntilFinished) {
				// times ++;

				//				changeButton();
				// int progress =
				// (int)(millisUntilFinished*catchMe.getTimeSpan())/catchMe.getTime();
				// Log.i("CatchMe", "Progress " + progress);
				// progressBar.setProgress(progress);
			}

			public void onFinish() {
				changeButton();
			}
		};

		return timer;
	}


	public void changeButton() {

		if (!catchMe.isCompleted()) {

			Button button;
			button = ((Button) findViewById(colorId));

			if (button == null)
				button = ((Button) findViewById(R.id.button1));
			else {
				// button.setBackgroundResource(R.drawable.round_button);
				button.setAlpha((float) 0.3);
			}
			Random buttonRand = new Random();

			color = buttonRand.nextInt(11) + 1;

			switch (color) {
			case 1:
				button = ((Button) findViewById(R.id.button1));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 2:
				button = ((Button) findViewById(R.id.button2));
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				button.setAlpha(1);
				break;

			case 3:
				button = ((Button) findViewById(R.id.button3));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 4:
				button = ((Button) findViewById(R.id.button4));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 5:
				button = ((Button) findViewById(R.id.button5));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 6:
				button = ((Button) findViewById(R.id.button6));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 7:
				button = ((Button) findViewById(R.id.button7));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 8:
				button = ((Button) findViewById(R.id.button8));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 9:
				button = ((Button) findViewById(R.id.button9));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 10:
				button = ((Button) findViewById(R.id.button10));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 11:
				button = ((Button) findViewById(R.id.button11));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			case 12:
				button = ((Button) findViewById(R.id.button12));
				button.setAlpha(1);
				// button.setBackgroundResource(R.drawable.round_button_lightup);
				break;

			}

			colorId = button.getId();

			// totalTimer.start();

		} else {
			// Log.i("CatchMe", "Termino");
			catchMe.finishChallenge();
			if (Factory.getInstance().getIDataManager().getHaveToSendScore()) {
				Thread t = new Thread() {
					public void run() {

						Factory.getInstance().getIDataManager().sendScore();
						Factory.getInstance().getIDataManager().updateRanking();
					}
				};

				t.start();
			}
			Intent challengeFinished = new Intent(this, CatchMeFinished.class);
			startActivity(challengeFinished);
			this.finish();
			StateDAO db = new StateDAO(this);
			db.updateState(Factory.getInstance().getIDataManager()
					.getState(CHALLENGE_ID));

			SharedPreferences sharedPrefs = getApplicationContext()
					.getSharedPreferences("asdasd_preferences",
							Context.MODE_PRIVATE);
			Editor editor = sharedPrefs.edit();
			editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
			editor.commit();
		}

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
							result = ((int) duration) + " "
									+ getResources().getString(R.string.week);
					} else {
						if (duration > 1)
							result = ((int) duration) + " "
									+ getResources().getString(R.string.days);
						else
							result = ((int) duration) + " "
									+ getResources().getString(R.string.day);
					}
				} else {
					if (duration > 1)
						result = ((int) duration) + " "
								+ getResources().getString(R.string.hours);
					else
						result = ((int) duration) + " "
								+ getResources().getString(R.string.hour);
				}
			} else {
				if (duration > 1)
					result = ((int) duration) + " "
							+ getResources().getString(R.string.minutes);
				else
					result = ((int) duration) + " "
							+ getResources().getString(R.string.minute);
			}
		} else {
			result = getResources().getString(R.string.few_seconds);
		}
		return result;
	}

	public void changeColorProgress() {
		int color;

		if (times <= 5)

			color = getResources().getColor(R.color.red);
		else if (times <= 10)
			color = getResources().getColor(R.color.yellow);
		else 
			color = getResources().getColor(R.color.green);
		

		// if (times <= 5 || (times > 15 && times <= 20) || (times > 30 && times
		// <= 35) || (times > 45 && times <= 50) || (times > 60 && times <= 65)
		// || (times > 75 && times <= 80))
		//
		// color = getResources().getColor(R.color.red);
		// else if (times <= 10 || (times > 20 && times <= 25)|| (times > 35 &&
		// times <= 40)|| (times > 50 && times <= 55) || (times > 65 && times <=
		// 70) || (times > 80 && times <= 85))
		// color = getResources().getColor(R.color.yellow);
		// else if ((times > 10 && times <= 15) || (times > 25 && times <= 30)
		// || (times > 40 && times <= 45) || (times > 55 && times <= 60) ||
		// (times > 70 && times <= 75) || (times > 85 && times <= 90))
		// color = getResources().getColor(R.color.green);
		// else
		// color = getResources().getColor(R.color.gris);

		switch (times) {

		case 1:
			((TextView) findViewById(R.id.TextView1)).setBackgroundColor(color);
			break;

		case 2:
		case 17:
		case 32:
		case 47:
		case 62:
		case 77:
		case 7:
		case 22:
		case 37:
		case 52:
		case 67:
		case 82:
		case 12:
		case 27:
		case 42:
		case 57:
		case 72:
		case 87:

			((TextView) findViewById(R.id.TextView2)).setBackgroundColor(color);
			break;
		case 3:
		case 18:
		case 33:
		case 48:
		case 63:
		case 78:
		case 8:
		case 23:
		case 38:
		case 53:
		case 68:
		case 83:
		case 13:
		case 28:
		case 43:
		case 58:
		case 73:
		case 88:
			((TextView) findViewById(R.id.TextView3)).setBackgroundColor(color);
			break;
		case 4:
		case 19:
		case 34:
		case 49:
		case 64:
		case 79:
		case 9:
		case 24:
		case 39:
		case 54:
		case 69:
		case 84:
		case 14:
		case 29:
		case 44:
		case 59:
		case 74:
		case 89:
			((TextView) findViewById(R.id.TextView4)).setBackgroundColor(color);
			break;
		case 5:
		case 20:
		case 35:
		case 50:
		case 65:
		case 80:
		case 10:
		case 25:
		case 40:
		case 55:
		case 70:
		case 85:
		case 15:
		case 30:
		case 45:
		case 60:
		case 75:
		case 90:
			((TextView) findViewById(R.id.TextView5)).setBackgroundColor(color);
			break;

		case 6:
		case 21:
		case 36:
		case 51:
		case 66:
		case 81:
		case 11:
		case 26:
		case 41:
		case 56:
		case 71:
		case 86:
		case 16:
		case 31:
		case 46:
		case 61:
		case 76:
			resetProgress();
			((TextView) findViewById(R.id.TextView1)).setBackgroundColor(color);
			break;
		// ((TextView)findViewById(R.id.TextView6)).setBackgroundColor(color);
		// break;
		// case 7:
		// case 22:
		// case 37:
		// case 52:
		// case 67:
		// case 82:
		// ((TextView)findViewById(R.id.TextView7)).setBackgroundColor(color);
		// break;
		// case 8:
		// case 23:
		// case 38:
		// case 53:
		// case 68:
		// case 83:
		// ((TextView)findViewById(R.id.TextView8)).setBackgroundColor(color);
		// break;
		// case 9:
		// case 24:
		// case 39:
		// case 54:
		// case 69:
		// case 84:
		// ((TextView)findViewById(R.id.TextView9)).setBackgroundColor(color);
		// break;
		// case 10:
		// case 25:
		// case 40:
		// case 55:
		// case 70:
		// case 85:
		// ((TextView)findViewById(R.id.TextView10)).setBackgroundColor(color);
		// break;
		// case 11:
		// case 26:
		// case 41:
		// case 56:
		// case 71:
		// case 86:
		// ((TextView)findViewById(R.id.TextView11)).setBackgroundColor(color);
		// break;
		// case 12:
		// case 27:
		// case 42:
		// case 57:
		// case 72:
		// case 87:
		// ((TextView)findViewById(R.id.TextView12)).setBackgroundColor(color);
		// break;
		// case 13:
		// case 28:
		// case 43:
		// case 58:
		// case 73:
		// case 88:
		// ((TextView)findViewById(R.id.TextView13)).setBackgroundColor(color);
		// break;
		// case 14:
		// case 29:
		// case 44:
		// case 59:
		// case 74:
		// case 89:
		// ((TextView)findViewById(R.id.TextView14)).setBackgroundColor(color);
		// break;
		// case 15:
		// case 30:
		// case 45:
		// case 60:
		// case 75:
		// case 90:
		// ((TextView)findViewById(R.id.TextView15)).setBackgroundColor(color);
		// break;
		// case 16:
		// case 31:
		// case 46:
		// case 61:
		// case 76:
		// resetProgress();
		// ((TextView)findViewById(R.id.TextView1)).setBackgroundColor(color);
		// break;
		}

	}

	@SuppressWarnings("deprecation")
	public void resetProgress() {
		((TextView) findViewById(R.id.TextView1))
				.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rectangle_border));
		((TextView) findViewById(R.id.TextView2))
				.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rectangle_border));
		((TextView) findViewById(R.id.TextView3))
				.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rectangle_border));
		((TextView) findViewById(R.id.TextView4))
				.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rectangle_border));
		((TextView) findViewById(R.id.TextView5))
				.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView6)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView7)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView8)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView9)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView10)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView11)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView12)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView13)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView14)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
		// ((TextView)findViewById(R.id.TextView15)).setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_border));
	}

}
