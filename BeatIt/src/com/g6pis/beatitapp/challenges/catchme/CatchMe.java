package com.g6pis.beatitapp.challenges.catchme;

import android.util.Log;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

public class CatchMe extends Challenge {

	private static final String CHALLENGE_ID = "7";

	private static final int TIME_LEVEL1 = 45000;
	private static final int TIME_LEVEL2 = 30000;
	
	private static final int TIME_SPAN_LEVEL1 = 500;
	private static final int TIME_SPAN_LEVEL2 = 500;
	
	
	private int time;

	private int timeSpan;
	

	private int successfulCount;
	private int unsuccessfulCount;
	
	private int totalCount;

	public CatchMe(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);

		this.successfulCount = 0;
		this.unsuccessfulCount = 0;

		switch (level) {
		case 1: {
			this.time = TIME_LEVEL1;
			this.timeSpan = TIME_SPAN_LEVEL1;
		
		}
			break;
		case 2: {
			this.time = TIME_LEVEL2;
			this.timeSpan = TIME_SPAN_LEVEL2;
		
		}
			break;
		}
		
		this.totalCount = (int) (this.time / this.timeSpan);
	}

	public int getTime() {
		return time;
	}

	public int getTimeSpan(){
		
		return timeSpan;
	}
	
	public void successful() {
		this.successfulCount++;
	}

	public void unsuccessful() {
		this.unsuccessfulCount++;
	}

	public int calculateScore(int good) {
		int score = 0;
		for (int i = 0; i < good; i++) {
			if (good < 5) {
				score += 1;
			} else if (good < 25) {
				score += 5;
			} else {
				score += 10;
			}
		}
		return score;

	}

	public void finishChallenge(int good) {
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore(good));

		reset();
	}

	public void reset() {
		this.successfulCount = 0;
		this.unsuccessfulCount = 0;
	}

	public int getCurrentCount() {
//		return this.successfulCount + this.unsuccessfulCount;
		
		return this.successfulCount;
	}

	public boolean isCompleted() {
		
		return (getCurrentCount() == totalCount) || (this.unsuccessfulCount > 0);
	}
}
