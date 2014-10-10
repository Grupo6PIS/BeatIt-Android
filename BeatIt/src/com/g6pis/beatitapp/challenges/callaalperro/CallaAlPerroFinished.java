package com.g6pis.beatitapp.challenges.callaalperro;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTDateTime;

public class CallaAlPerroFinished extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_finished);

		// double maxSpeed = getIntent().getExtras().getString("maxSpeed");
		// double avgSpeed = getIntent().getExtras().getString("avgSpeed");
		// double score = getIntent().getExtras().getString("score");
		
		this.editLayout();
       /* findViewById(R.id.homeButton).setOnClickListener(this);
        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);*/
		
		((TextView)findViewById(R.id.textView_last_sscore_value)).setText(getIntent().getExtras().getString("score") + " " +getResources().getString(R.string.points));
		//TODO max score, fecha, etc desde DataManager
		
		
		((TextView) findViewById(R.id.textView_Start_Time_Value))
		.setText(getIntent().getExtras().getString("dateTimeStart"));
		
		DTDateTime finishDate = new DTDateTime();
		finishDate.setDay(getIntent().getExtras().getInt("day"));
		finishDate.setMonth(getIntent().getExtras().getInt("month"));
		finishDate.setYear(getIntent().getExtras().getInt("year"));
		finishDate.setHour(getIntent().getExtras().getInt("hours"));
		finishDate.setMinute(getIntent().getExtras().getInt("minutes"));
		finishDate.setSecond(getIntent().getExtras().getInt("seconds"));
		
		((TextView)findViewById(R.id.textView_Time_Finish_Value)).setText(finishDate.toString());

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
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
	}
	
	
	public void editLayout() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.callaalperro)));
		
		((ImageView)findViewById(R.id.imageView_Logo)).setImageDrawable(getResources().getDrawable(R.drawable.ic_calla_al_perro));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setText(getResources().getString(R.string.calla_al_perro));
		((TextView)findViewById(R.id.textView_Challenge_Name)).setTextColor(getResources().getColor(R.color.callaalperro));
		((TableRow)findViewById(R.id.text_row)).setBackgroundColor(getResources().getColor(R.color.callaalperro));

	}
}
