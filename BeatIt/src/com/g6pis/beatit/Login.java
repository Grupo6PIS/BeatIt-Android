package com.g6pis.beatit;

import com.g6pis.beatit.challenges.UsainBolt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class Login extends Activity {
	private static final String APP_SHARED_PREFS = "asdasd_preferences";
    SharedPreferences sharedPrefs;
    Editor editor;
    private boolean isUserLoggedIn;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		this.getActionBar().hide();
	
		
		final LinearLayout ll = (LinearLayout) findViewById(R.id.linear_facebook);
        ll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	/*Intent challenge = new Intent(Login.this, Home.class);
            	startActivity(challenge);*/
            	
            	sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
                editor = sharedPrefs.edit();
                editor.putBoolean("userLoggedInState", true);
                editor.commit();

                Intent signupSuccessHome = new Intent(Login.this, Home.class);
                signupSuccessHome.putExtra("reqFrom", "login");
                startActivity(signupSuccessHome);
                finish();

            	
            }
        });
//        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
//	    if (isUserLoggedIn) {
//	        Intent intent = new Intent(this, Home.class);
//	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        startActivity(intent);
//	        finish();
//	    }
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
