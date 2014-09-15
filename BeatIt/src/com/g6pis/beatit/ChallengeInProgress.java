/*package com.g6pis.beatit;

import com.g6pis.beatit.challenges.UsainBolt;
import com.g6pis.beatit.persistence.UsainBoltDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ChallengeInProgress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_in_progress);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		UsainBoltDAO db = new UsainBoltDAO(this);

		UsainBolt challenge = new UsainBolt();
		challenge.setChallengeId(0);
		challenge.setName("Usain Bolt");
		challenge
				.setDescription("Debes llegar a la velocidad 6km/h en el menor tiempo posible.");
		challenge.setDuration(1);
		challenge.setLevel(1);

		db.addUsainBolt(challenge);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge_in_progress, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void usainBoltChallenge(View view) {
		// Toast.makeText(this, "Click", Toast.LENGTH_LONG).show();;
		Intent intent = new Intent(ChallengeInProgress.this, UsainBolt.class);
		startActivity(intent);
	}

}*/
