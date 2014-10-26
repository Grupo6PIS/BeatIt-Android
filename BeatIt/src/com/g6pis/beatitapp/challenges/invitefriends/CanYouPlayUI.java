package com.g6pis.beatitapp.challenges.invitefriends;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.persistence.StateDAO;

public class CanYouPlayUI extends Activity implements OnClickListener {
	private static final String CHALLENGE_ID = "3";
	private static final int PICK_CONTACT = 10;
	private static final int CHALLENGE_COMPLETED_DIALOG = 70;
	private static final int FACEBOOK_DIALOG = 60;

	private UiLifecycleHelper uiHelper;
	private ImageButton facebookButton;
	private ImageButton smsButton;
	private ImageButton selectContactButton;
	private ImageButton doneButton;

	private String phone;

	private CanYouPlay canYouPlay;
	private DTState state;

	private Dialog challengeNotCompletedDialog;
	private Dialog facebookDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.can_you_play);


		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		challengeNotCompletedDialog = onCreateDialog(CHALLENGE_COMPLETED_DIALOG);
		challengeNotCompletedDialog.hide();
		facebookDialog = onCreateDialog(FACEBOOK_DIALOG);
		facebookDialog.hide();
		
		
		
		facebookButton = (ImageButton) findViewById(R.id.facebook_post_button);
		smsButton = (ImageButton) findViewById(R.id.send_SMS_button);
		selectContactButton = (ImageButton) findViewById(R.id.select_contact_button);
		doneButton = (ImageButton) findViewById(R.id.done_button);
		doneButton.setOnClickListener(this);
		selectContactButton.setOnClickListener(this);
		facebookButton.setOnClickListener(this);
		smsButton.setOnClickListener(this);

		canYouPlay = (CanYouPlay) DataManager.getInstance().getChallenge(
				CHALLENGE_ID);
		state = DataManager.getInstance().getState(CHALLENGE_ID);
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

		switch (canYouPlay.getLevel()) {
		case 1:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.can_you_play_description_1));
			break;
		case 2:
			((TextView) findViewById(R.id.textView_Description_Value_2))
					.setText(getResources().getString(
							R.string.can_you_play_description_2));
			break;
		}
		this.editActionBar();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_CONTACT) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();

				Cursor c = null;
				Cursor phonesCursor = null;
				c = getContentResolver().query(uri, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						phonesCursor = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phonesCursor.moveToFirst();
						int phoneType = phonesCursor.getInt(phonesCursor
								.getColumnIndex(Phone.TYPE));
						if (phoneType == Phone.TYPE_MOBILE) {
							phone = phonesCursor
									.getString(phonesCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

							if (!canYouPlay.addPhone(phone)) {
								Toast.makeText(
										getApplicationContext(),
										getResources()
												.getString(
														R.string.select_diferent_contact),
										Toast.LENGTH_LONG).show();
							} else {
								selectContactButton
										.setVisibility(View.INVISIBLE);
								smsButton.setVisibility(View.VISIBLE);
							}

						}
					} else {
						Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.select_another_contact),
								Toast.LENGTH_LONG).show();
					}
				}

			}
		} else {
			uiHelper.onActivityResult(requestCode, resultCode, data,
					new FacebookDialog.Callback() {
						@Override
						public void onError(
								FacebookDialog.PendingCall pendingCall,
								Exception error, Bundle data) {
							Log.e("Activity", String.format("Error: %s",
									error.toString()));
						}

						@Override
						public void onComplete(
								FacebookDialog.PendingCall pendingCall,
								Bundle data) {

							String completionGesture = FacebookDialog
									.getNativeDialogCompletionGesture(data);
							if (!completionGesture.equals("cancel")) {
								canYouPlay.fbPost();
							}
						}
					});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {	
		case R.id.facebook_post_button:
			postOnFacebook();
			break;
		case R.id.select_contact_button:
			selectContact();
			break;
		case R.id.send_SMS_button:
			sendSms();
			break;
		case R.id.done_button:
			if (!canYouPlay.isCompleted())
				challengeNotCompletedDialog.show();
			else
				completeChallenge();
			break;
		}

	}

	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setLogo(getResources().getDrawable(R.drawable.app_logo));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(canYouPlay.getColor())));

	}

	public void postOnFacebook() {
		try {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					this)
					.setLink(
							"https://play.google.com/store/apps/details?id=com.g6pis.beatitapp")
					.setName(getResources().getString(R.string.sms_text))
					.setDescription(
							getResources().getString(R.string.android_version))
					.setCaption(
							getResources().getString(R.string.also_available))
					.setPicture(
							"https://lh3.googleusercontent.com/Z0gp_Vw-g3ZI9ewq5MRHnNITqDpEDtWN6eh_j28UHiMkY_9b-4K5OFMVd6GWO40hdS-oVAI0Nw=w1893-h822")
					.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		} catch (FacebookException ex) {
			facebookDialog.show();
			((Button) findViewById(R.id.facebook_post_button))
					.setClickable(false);
			canYouPlay.fbPost();

		}
	}

	public void selectContact() {

		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);
	}

	public void sendSms() {
		selectContactButton.setVisibility(View.VISIBLE);
		smsButton.setVisibility(View.INVISIBLE);
		try {
			// Get the default instance of the SmsManager

			SmsManager smsManager = SmsManager.getDefault();

			smsManager
					.sendTextMessage(
							phone,
							null,
							getResources().getString(R.string.sms_text)
									+ "\n\nAndroid:\nhttps://play.google.com/store/apps/details?id=com.g6pis.beatitapp0"
									+ "\n\n"
									+ getResources().getString(
											R.string.also_available), null,
							null);

			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.sms_success),
					Toast.LENGTH_LONG).show();

			canYouPlay.smsSent();

		} catch (Exception ex) {

			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.sms_failed),
					Toast.LENGTH_LONG).show();

		}
		phone = "";

	}

	public void completeChallenge() {
		Intent finished = new Intent(this, CanYouPlayFinished.class);
		startActivity(finished);
		this.finish();
		canYouPlay.finishChallenge();
		StateDAO db = new StateDAO(this);
		db.updateState(DataManager.getInstance().getState(CHALLENGE_ID));
	}

	@Override
	public void onBackPressed() {
		canYouPlay.reset();
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
			canYouPlay.reset();
			Intent home = new Intent(this, Home.class);
			startActivity(home);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case CHALLENGE_COMPLETED_DIALOG: {
			builder.setMessage(R.string.want_to_continue);
			builder.setTitle(getResources().getString(
					R.string.challenge_not_completed));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_button,
					new OkOnClickListener());
			builder.setNegativeButton(R.string.cancel,
					new CancelOnClickListener());
			return builder.create();
		}
		case FACEBOOK_DIALOG:
			builder.setMessage(R.string.facebook_app_not_installed);
			builder.setTitle(getResources().getString(
					R.string.facebook_post));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_button,
					new OkOnClickListener());
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			completeChallenge();
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
	

}
