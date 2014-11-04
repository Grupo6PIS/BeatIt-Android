package com.g6pis.beatitapp.challenges.selfiegroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.interfaces.Factory;
import com.g6pis.beatitapp.persistence.StateDAO;

public class SelfieGroupUI extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "10";

	static final int REQUEST_IMAGE_CAPTURE = 100;
	static final int LIGHT_DIALOG = 50;

	private DTState state;
	private SelfieGroup selfieGroup;

	private Button startButton;
	private Bitmap cameraBitmap;
	Bitmap tmpBmp;
	private Dialog lightDialog;
	
	
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;   
	private static final int MAX_FACES = 50;
	private static String TAG = "SelfieGroupUI";
	protected static final int GUIUPDATE_SETFACE = 999;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfie_group);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

		lightDialog = onCreateDialog(LIGHT_DIALOG);
		lightDialog.hide();

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
		startButton
				.setBackgroundColor(Color.parseColor(selfieGroup.getColor()));
		((TextView) findViewById(R.id.textView_Challenge)).setTextColor(Color
				.parseColor(selfieGroup.getColor()));

	}

	@Override
	public void onClick(View v) {
		lightDialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			cameraBitmap = (Bitmap) data.getExtras().get("data");
			cameraBitmap = cameraBitmap.copy(Config.RGB_565, true);
			int picWidth = cameraBitmap.getWidth();
//			int picHeight = cameraBitmap.getHeight();
			if (picWidth % 2 != 0) {
				picWidth = picWidth - 1;
			}
			
			mFaceWidth = picWidth;
			mFaceHeight = cameraBitmap.getHeight();  
			
			
			tmpBmp = cameraBitmap.copy(Config.RGB_565, true);
			tmpBmp = Bitmap.createBitmap(tmpBmp, 0, 0, picWidth, cameraBitmap.getHeight());
					
			// perform face detection in setFace() in a background thread
			doLengthyCalc();

//			FaceDetector faceDet = new FaceDetector(tmpBmp.getWidth(),
//					tmpBmp.getHeight(), 50);
//			Face[] faceList = new Face[50];
//			selfieGroup.setPeople(faceDet.findFaces(tmpBmp, faceList));
			
			startButton.setText(getResources().getString(R.string.calculating));
			startButton.setClickable(false);
			

		}
	}
	
	  private void doLengthyCalc() {
	    	Thread t = new Thread() {
	    		
	    		public void run() {
	    			try {
	    				setFace();
	    				
	    			} catch (Exception e) { 
	    				Log.e(TAG, "doLengthyCalc(): " + e.toString());
	    			}
	    		}
	    	};      

	    	t.start();        
	    }  
	
	 public void setFace() {
	    	FaceDetector fd;
	    	FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
	    	
	    	int count = 0;
	    	
	    	try {
	    		fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);        
	    		count = fd.findFaces(tmpBmp, faces);
	    		
	    		
	    		Log.e(TAG, "setFace(): " + count);
	    		
	    	} catch (Exception e) {
	    		Log.e(TAG, "setFace(): " + e.toString());
	    		return;
	    	}
	    	
	    	selfieGroup.setPeople(count);
	    	challengeComplete();
	 }

	public void challengeComplete() {
		selfieGroup.finishChallenge();

		Intent intent = new Intent(this, SelfieGroupFinished.class);
		startActivity(intent);
		finish();

		StateDAO db = new StateDAO(getApplicationContext());
		db.updateState(Factory.getInstance().getIDataManager()
				.getState(CHALLENGE_ID));
		
		SharedPreferences sharedPrefs = getApplicationContext()
				.getSharedPreferences("asdasd_preferences",
						Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean("haveToSendScore", Factory.getInstance().getIDataManager().getHaveToSendScore());
		editor.commit();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case LIGHT_DIALOG: {
			builder.setMessage(R.string.light_dialog);
			builder.setTitle(getResources().getString(
					R.string.attention));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_button,
					new OkOnClickListener());
		}
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
			
		}
	}
	

}
