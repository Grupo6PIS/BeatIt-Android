package com.g6pis.beatitapp.challenges.throwthephone;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class ThrowThePhone extends Challenge {
	private String CHALLENGE_ID = "6";
	
	private static int MIN_HEIGHT_1 = 100;
	private static int MIN_HEIGHT_2 = 200;

	private int minHeight;
	private int heightReached;

	public ThrowThePhone(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);

		heightReached = 0;

		switch (level) {
		case 1:
			minHeight = MIN_HEIGHT_1;
			break;
		case 2:
			minHeight = MIN_HEIGHT_2;
			break;
		}
	}

	public double getHeightReached() {
		return heightReached;
	}

	public void setHeightReached(int heightReached) {
		this.heightReached = heightReached;
	}

	public int getMinHeight() {
		return minHeight;
	}
	
	public int calculateScore(){
		int score = 0;
		if(heightReached >= minHeight){
			score = 60 + (heightReached - minHeight);
		}
		
		return score;
	}
	
	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
	}
	
}
