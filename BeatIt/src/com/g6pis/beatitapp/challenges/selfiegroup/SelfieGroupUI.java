package com.g6pis.beatitapp.challenges.selfiegroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.challenges.selfiegroup.SelfieGroup;
import com.g6pis.beatitapp.challenges.selfiegroup.SelfieGroupUI;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColor;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

public class SelfieGroupUI extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "10";
	
	static final int REQUEST_IMAGE_CAPTURE = 100;

	private DTState state;
	private SelfieGroup selfieGroup;

	private Button startButton;
	private Bitmap cameraBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfie_group);

		selfieGroup = (SelfieGroup) Factory.getInstance().getIDataManager()
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

		switch (selfieGroup.getLevel()) {
		case 1:
			((TextView) findViewById(R.id.textView_Description_Value))
					.setText(getResources().getString(
							R.string.description_selfie_group_1));
			break;
		case 2:
			((TextView) findViewById(R.id.textView_Description_Value))
					.setText(getResources().getString(
							R.string.description_selfie_group_2));
			break;
		}

		startButton = (Button) findViewById(R.id.start_challenge_button);
		startButton.setOnClickListener(this);

		this.editActionBar();

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
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.week);
					} else {
						if (duration > 1)
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.days);
						else
							result = ((int) duration)+ " "
									+ getResources().getString(R.string.day);
					}
				} else {
					if (duration > 1)
						result = ((int) duration)+ " "
								+ getResources().getString(R.string.hours);
					else
						result = ((int) duration)+ " "
								+ getResources().getString(R.string.hour);
				}
			} else {
				if (duration > 1)
					result = ((int) duration)+ " "
							+ getResources().getString(R.string.minutes);
				else
					result = ((int) duration)+ " "
							+ getResources().getString(R.string.minute);
			}
		} else {
			result = getResources().getString(R.string.few_seconds);
		}
		return result;
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
				.parseColor(selfieGroup.getColor())));
		startButton.setBackgroundColor(Color.parseColor(selfieGroup.getColor()));
		((TextView)findViewById(R.id.textView_Challenge)).setTextColor(Color.parseColor(selfieGroup.getColor()));

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    
	    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        cameraBitmap = (Bitmap) data.getExtras().get("data");
	        
	        int picWidth = cameraBitmap.getWidth();
			int picHeight= cameraBitmap.getHeight();
			if(picWidth % 2 != 0){
				picWidth = picWidth -1;
			}
			Bitmap tmpBmp = cameraBitmap.copy(Config.RGB_565, true);
			tmpBmp = Bitmap.createBitmap(tmpBmp,0,0,picWidth,picHeight);
			
					
			FaceDetector faceDet = new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 50);
			Face[] faceList = new Face[50];
			
			selfieGroup.setPeople(faceDet.findFaces(tmpBmp,faceList));
			challengeComplete();

	    }
	}
	
	public void challengeComplete(){
		selfieGroup.finishChallenge();
		
		Intent intent = new Intent(this, SelfieGroupFinished.class);
		startActivity(intent);
		finish();
		
		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(Factory.getInstance().getIDataManager().getState(CHALLENGE_ID));
	}

}
