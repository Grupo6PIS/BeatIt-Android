package com.g6pis.beatitapp.challenges.bouncinggame;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class BouncingGame extends Challenge {

	private static final String CHALLENGE_ID 		= "5";
	
	private int succeed_times;
	
	public BouncingGame(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name, level, 999,color);

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
	
	public int getSucceed_times() {
		return succeed_times;
	}


	public void setSucceed_times(int succeed_times) {
		this.succeed_times = succeed_times;
	}

	public void reset(){
		this.succeed_times = 0;
	}

	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
	}

	//@Override
	public double calculateScore(){
		return (succeed_times)*10;
	}
	
}
