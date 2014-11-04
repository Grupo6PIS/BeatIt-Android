package com.g6pis.beatitapp.challenges.catchme;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGameUI;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColor;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColorUI;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class CatchMeFinished extends Activity implements OnClickListener {

	private static final String CHALLENGE_ID = "7";
	private static final int CHALLENGE_DIALOG = 50;

	private DTState state;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.challenge_finished);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);

		this.editLayout();

		((TextView) findViewById(R.id.textView_last_sscore_value))
				.setText(Double.toString(state.getLastScore()));
		((TextView) findViewById(R.id.textView_max_score_value)).setText(Double
				.toString(state.getMaxScore()));
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());

		Challenge challenge = Factory.getInstance().getIDataManager()
				.getChallenge(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Attempts_Value)).setText(state
				.getCurrentAttempt() + "/" + challenge.getMaxAttempt());
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

	@Override
	public void onBackPressed() {
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
	}

	public void editLayout() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));

		CatchMe catchMe = (CatchMe) Factory.getInstance().getIDataManager()
				.getChallenge(CHALLENGE_ID);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(catchMe.getColor())));

		((ImageView) findViewById(R.id.imageView_Logo))
				.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_catch_me));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setText(getResources().getString(R.string.catch_me));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setTextColor(Color.parseColor(catchMe.getColor()));
		((TableRow) findViewById(R.id.text_row)).setBackgroundColor(Color
				.parseColor(catchMe.getColor()));

		((ImageButton) findViewById(R.id.refresh_button))
				.setVisibility(View.INVISIBLE);

		if (!state.isFinished()) {
			((ImageButton) findViewById(R.id.retry_button))
					.setOnClickListener(this);
			((ImageButton) findViewById(R.id.retry_button))
					.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CatchMeUI.class);
		startActivity(intent);
		finish();

	}


}
