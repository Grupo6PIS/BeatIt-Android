package com.g6pis.beatitapp.challenges.songcomplete;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

public class SongComplete extends Challenge {

	private static final String CHALLENGE_ID = "5";
	
	private long succeed_times;
	
	public SongComplete(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);

			switch (level) {

			case 1: {
				this.succeed_times = 0;
			}
				break;
			case 2: {
				this.succeed_times = 0;
			}
				break;
			}
		
	}

	public void reset(){
		this.succeed_times = 0;
	}

	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
	}

	//@Override
	public double calculateScore(){
		return (succeed_times)*5;
	}
	
}
