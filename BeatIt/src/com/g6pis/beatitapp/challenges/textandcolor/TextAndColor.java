package com.g6pis.beatitapp.challenges.textandcolor;

import java.util.ArrayList;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

public class TextAndColor extends Challenge {
	private static final String CHALLENGE_ID = "8";
	
	private static final int TIME_LEVEL1 = 1700;
	private static final int TIME_LEVEL2 = 1500;
	private static final int COUNT_LEVEL1 = 15;
	private static final int COUNT_LEVEL2 = 30;
	private static final int MIN_COUNT_LEVEL1 = 6;
	private static final int MIN_COUNT_LEVEL2 = 15;
	
	private int time;
	private int count;
	private int minCount;
	
	private int successfulCount;
	private int unsuccessfulCount;
	
	
	public TextAndColor(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);
		
		this.successfulCount = 0;
		
		switch(level){
		case 1:{
			this.time = TIME_LEVEL1;
			this.count = COUNT_LEVEL1;
			this.minCount = MIN_COUNT_LEVEL1;
		}break;
		case 2:{
			this.time = TIME_LEVEL2;
			this.count = COUNT_LEVEL2;
			this.minCount = MIN_COUNT_LEVEL2;
		}break;
		}
		
	}

	public int getTime() {
		return time;
	}

	public int getCount() {
		return count;
	}

	public int getMinCount() {
		return minCount;
	}

	public void successful() {
		this.successfulCount++;
	}
	
	public void unsuccessful(){
		this.unsuccessfulCount++;
	}
	
	public int calculateScore(){
		int score = (this.successfulCount - minCount)*8;
		if(score > 0)
			return score;
		else
			return 0;
	}
	
	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
		reset();
	}
	
	public void reset(){
		this.successfulCount = 0;
		this.unsuccessfulCount = 0;
	}
	
	public int getCurrentCount(){
		return this.successfulCount + this.unsuccessfulCount;
	}
	
	public boolean isCompleted(){
		return getCurrentCount() == count;
	}
	
	
}
