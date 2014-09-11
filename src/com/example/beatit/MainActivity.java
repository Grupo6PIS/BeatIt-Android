package com.example.beatit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*        if (id == R.id.action_settings) {
            return true;
        }
*/
        switch (id) {
	        case R.id.menu_back:
	            Log.i("ActionBar", "Back");
	            return true;
	        case R.id.action_settings:
	            Log.i("ActionBar", "Settings!");;
	            return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	    }
    }
}
