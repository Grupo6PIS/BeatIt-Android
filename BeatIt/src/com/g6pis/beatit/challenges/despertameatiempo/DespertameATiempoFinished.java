package com.g6pis.beatit.challenges.despertameatiempo;

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
import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.datatypes.DTState;

public class DespertameATiempoFinished extends Activity  {
	private static final String CHALLENGE_ID = "2";
	
	private DTState state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);
		

		this.editLayout();
		
		((TextView)findViewById(R.id.textView_last_sscore_value)).setText(getIntent().getExtras().getString("score") + " " +getResources().getString(R.string.points));
		// TODO cuando esté la persistencia - Max. Score
		((TextView)findViewById(R.id.textView_max_score_value)).setText(getIntent().getExtras().getString("score") + " " +getResources().getString(R.string.points));
		
		((TextView) findViewById(R.id.textView_Start_Time_Value))
			.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		//((TextView)findViewById(R.id.textView_Time_Finish_Value)).setText(finishDate.toString());
		
		
		//state = DataManager.getInstance().getState(CHALLENGE_ID);
		//((TextView) findViewById(R.id.textView_Finish_Time_Value)).setText(state.getLastFinishDateTime().toString());
		

/* ULTIMO *******************		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.despertame)));
*/		
		
       /* findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);*/
		
		//TODO pedirle el state correspondiente al DataManager
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


/* ULTIMO **********************
        if (getIntent().getExtras().getBoolean("resultado") == true) {
        	((TextView) findViewById(R.id.tiempo)).setText("Ganaste !");
        }
        else {
        	((TextView) findViewById(R.id.tiempo)).setText("Perdiste ;(");
        }
		

		((TextView) findViewById(R.id.tiempo2))
			.setText(getIntent().getExtras().getString("cantExitos") + " exitos");
		
		((TextView) findViewById(R.id.textView_attemps))
			.setText(getIntent().getExtras().getLong("attemps") + "/3");

		((TextView) findViewById(R.id.textView_Score_Value))
			.setText(getIntent().getExtras().getString("score") + getResources().getString(R.string.points));
		
		((TextView) findViewById(R.id.textView_Start_Time_Value))
			.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		((TextView)findViewById(R.id.textView_Duration_Value)).setText(finishDate.toString());
*/		
		

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
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.despertame)));
		
		((ImageView)findViewById(R.id.imageView_Logo)).setImageDrawable(getResources().getDrawable(R.drawable.ic_despertame_a_tiempo));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setText(getResources().getString(R.string.despertame_a_tiempo));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setTextColor(getResources().getColor(R.color.despertame));
		((TableRow)findViewById(R.id.text_row)).setBackgroundColor(getResources().getColor(R.color.despertame));

	}
	
}