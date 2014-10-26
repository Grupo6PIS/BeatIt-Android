package com.g6pis.beatitapp.challenges.songcomplete;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class SongComplete extends Challenge {

	private static final String CHALLENGE_ID = "9";
	
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

	public long getSucceed_times() {
		return succeed_times;
	}

	public void setSucceed_times(long succeed_times) {
		this.succeed_times = succeed_times;
	}

	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
	}

	//@Override
	public double calculateScore(){
		return (succeed_times)*40;
	}
	
}
