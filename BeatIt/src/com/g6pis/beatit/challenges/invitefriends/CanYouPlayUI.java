package com.g6pis.beatit.challenges.invitefriends;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.g6pis.beatit.Home;
import com.g6pis.beatit.R;

public class CanYouPlayUI extends Activity implements OnClickListener {
	private static final int PICK_CONTACT = 10;

	private UiLifecycleHelper uiHelper;
	private Button facebookButton;
	private Button smsButton;
	private Button selectContactButton;
	private Button doneButton;

	private String phone;
	private int count;
	private List<String> phones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.can_you_play);

		this.editActionBar();

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		facebookButton = (Button) findViewById(R.id.facebook_post_button);
		smsButton = (Button) findViewById(R.id.send_SMS_button);
		selectContactButton = (Button) findViewById(R.id.select_contact_button);
		doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setOnClickListener(this);
		selectContactButton.setOnClickListener(this);
		facebookButton.setOnClickListener(this);
		smsButton.setOnClickListener(this);

		count = 0;
		phones = new ArrayList<String>();

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

							if (phones.contains(phone)) {
								Toast.makeText(getApplicationContext(),
										"Select a different contact",
										Toast.LENGTH_LONG).show();
							} else {
								selectContactButton
										.setVisibility(View.INVISIBLE);
								smsButton.setVisibility(View.VISIBLE);
							}

						}
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
								Log.i("Activity", "success");
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
			completeChallenge();
			break;
		}

	}

	public void editActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getString(R.string.app_name));
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.invita_amigos)));

	}

	public void postOnFacebook() {
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
				.setLink(
						"https://play.google.com/store/apps/details?id=com.g6pis.beatit&ah=mcoN2TjRF_obuPnlAcKtanl9mFk")
				.setName(getResources().getString(R.string.sms_text))
				.setDescription(
						getResources().getString(R.string.android_version))
				.setCaption(
						getResources().getString(R.string.also_available))
				.setPicture("https://lh3.googleusercontent.com/Z0gp_Vw-g3ZI9ewq5MRHnNITqDpEDtWN6eh_j28UHiMkY_9b-4K5OFMVd6GWO40hdS-oVAI0Nw=w1893-h822")
				.build();
		uiHelper.trackPendingDialogCall(shareDialog.present());
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
									 + "\n\nAndroid:\nhttps://play.google.com/store/apps/details?id=com.g6pis.beatit&ah=mcoN2TjRF_obuPnlAcKtanl9mFk"
									 + "\n\n"+getResources().getString(R.string.also_available) ,
							null, null);

			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.sms_success),
					Toast.LENGTH_LONG).show();

		} catch (Exception ex) {

			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.sms_failed),
					Toast.LENGTH_LONG).show();

		}
		count++;
		phone = "";

	}

	public void completeChallenge() {
		Intent finished = new Intent(this, CanYouPlayFinished.class);
		// TODO calcular el puntaje mediante la l�gica
		startActivity(finished);
		this.finish();
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

}
