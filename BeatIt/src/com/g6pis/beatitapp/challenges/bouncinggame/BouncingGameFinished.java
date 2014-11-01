package com.g6pis.beatitapp.challenges.bouncinggame;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class BouncingGameFinished extends Activity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {
	private static final String CHALLENGE_ID = "5";
	private static final int CHALLENGE_DIALOG = 50;

	private DTState state;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);

		this.editLayout();

		String lastScore = Integer.toString(((Long) Math.round(state
				.getLastScore())).intValue());
		String maxScore = Integer.toString(((Long) Math.round(state
				.getMaxScore())).intValue());
		((TextView) findViewById(R.id.textView_last_sscore_value))
				.setText(lastScore);
		((TextView) findViewById(R.id.textView_max_score_value))
				.setText(maxScore);
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());

		Challenge challenge = DataManager.getInstance().getChallenge(
				CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Attempts_Value)).setText(state
				.getCurrentAttempt() + "/" + challenge.getMaxAttempt());

		dialog = onCreateDialog(CHALLENGE_DIALOG);
		dialog.hide();

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
			dialog.dismiss();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		dialog.show();

	}

	@Override
	public void onBackPressed() {
		dialog.dismiss();
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
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.bouncing)));

		((ImageView) findViewById(R.id.imageView_Logo))
				.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_bouncing_game));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setText(getResources().getString(R.string.bouncing_game));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setTextColor(getResources().getColor(R.color.bouncing));
		((TableRow) findViewById(R.id.text_row))
				.setBackgroundColor(getResources().getColor(R.color.bouncing));

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
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case CHALLENGE_DIALOG: {
			builder.setMessage(R.string.challenge_under_construction);
			builder.setTitle(getResources().getString(
					R.string.challenge_under_construction_title));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_button, this);
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}

		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent intent = new Intent(this, BouncingGameUI.class);
		startActivity(intent);
		this.finish();

	}

}