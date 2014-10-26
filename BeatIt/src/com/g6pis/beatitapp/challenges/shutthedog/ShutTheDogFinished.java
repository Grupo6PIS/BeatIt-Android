package com.g6pis.beatitapp.challenges.shutthedog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class ShutTheDogFinished extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "4";

	private DTState state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);

		state = Factory.getInstance().getIDataManager().getState(CHALLENGE_ID);

		this.editLayout();

		((TextView) findViewById(R.id.textView_last_sscore_value))
				.setText(Double.toString(state.getLastScore()));
		((TextView) findViewById(R.id.textView_max_score_value)).setText(Double
				.toString(state.getMaxScore()));
		((TextView) findViewById(R.id.textView_Start_Time_Value)).setText(state
				.getDateTimeStart().toString());
		
		Challenge challenge = Factory.getInstance().getIDataManager().getChallenge(CHALLENGE_ID);
		((TextView) findViewById(R.id.textView_Attempts_Value))
		.setText(state.getCurrentAttempt() + "/" + challenge.getMaxAttempt());

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

	public void onClick(View v) {
		Intent intent = new Intent(this, ShutTheDogUI.class);
		startActivity(intent);
		finish();
	}

	public void editLayout() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.shutthedog)));

		((ImageView) findViewById(R.id.imageView_Logo))
				.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_calla_al_perro));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setText(getResources().getString(R.string.shut_the_dog));
		((TextView) findViewById(R.id.textView_Challenge_Name))
				.setTextColor(getResources().getColor(R.color.shutthedog));
		((TableRow) findViewById(R.id.text_row))
				.setBackgroundColor(getResources().getColor(R.color.shutthedog));

		((ImageButton) findViewById(R.id.refresh_button))
				.setVisibility(View.INVISIBLE);

		if (!state.isFinished()) {
			((ImageButton) findViewById(R.id.retry_button))
					.setOnClickListener(this);
			((ImageButton) findViewById(R.id.retry_button))
					.setVisibility(View.VISIBLE);
		}

	}
}
