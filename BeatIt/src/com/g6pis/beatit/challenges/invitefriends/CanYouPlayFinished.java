package com.g6pis.beatit.challenges.invitefriends;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.g6pis.beatit.Home;
import com.g6pis.beatit.R;
import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.datatypes.DTState;

public class CanYouPlayFinished extends Activity {
	private static final String CHALLENGE_ID = "3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);

		
       /* findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);*/
		
		//TODO pedirle el state correspondiente al DataManager
		DTState state = DataManager.getInstance().getState(CHALLENGE_ID);
		
		
		this.editLayout();
		
		((TextView)findViewById(R.id.textView_last_sscore_value)).setText(Double.toString(state.getLastScore()));
		((TextView)findViewById(R.id.textView_max_score_value)).setText(Double.toString(state.getMaxScore()));
		
		
		/*((TextView) findViewById(R.id.sms_sent_value))
				.setText(getIntent().getExtras().getString("sms"));

		((TextView) findViewById(R.id.fb_post_value))
				.setText(getIntent().getExtras().getString("fb"));

		((TextView) findViewById(R.id.textView_Score_Value))
				.setText(getIntent().getExtras().getString("score") + getResources().getString(R.string.points));*/
		
		/*((TextView) findViewById(R.id.textView_Start_Time_Value))
		.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		((TextView)findViewById(R.id.textView_Duration_Value)).setText(finishDate.toString());*/

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
	public void onBackPressed(){
		Intent home = new Intent(this, Home.class);
		startActivity(home);
		this.finish();
		super.onBackPressed();
	}
	
	public void editLayout() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.can_you_play)));
		
		((ImageView)findViewById(R.id.imageView_Logo)).setImageDrawable(getResources().getDrawable(R.drawable.ic_can_you_play));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setText(getResources().getString(R.string.can_you_play));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setTextColor(getResources().getColor(R.color.can_you_play));
		((TableRow)findViewById(R.id.text_row)).setBackgroundColor(getResources().getColor(R.color.can_you_play));

	}
}
