package com.g6pis.beatitapp.challenges.wakemeup;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class WakeMeUp extends Challenge {
	private static final String CHALLENGE_ID = "2";

	private long veces_exito;
		
	public WakeMeUp(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name,level, maxAttempt);

		this.veces_exito = 0;
			
	}

	public long getVeces_exito() {
		return veces_exito;
	}


	public void setVeces_exito(long veces_exito) {
		this.veces_exito = veces_exito;
	}

	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
	}

	//@Override
	public double calculateScore(){
		return (veces_exito)*20;
	}
		
}
