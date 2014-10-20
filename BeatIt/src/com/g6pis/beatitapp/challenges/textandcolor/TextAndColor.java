package com.g6pis.beatitapp.challenges.textandcolor;

import java.util.ArrayList;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

public class TextAndColor extends Challenge {
	private static final String CHALLENGE_ID = "8";
	
	private static final int TIME_LEVEL1 = 700;
	private static final int TIME_LEVEL2 = 500;
	private static final int COUNT_LEVEL1 = 10;
	private static final int COUNT_LEVEL2 = 20;
	private static final int MIN_COUNT_LEVEL1 = 5;
	private static final int MIN_COUNT_LEVEL2 = 10;
	
	private int time;
	private int count;
	private int minCount;
	
	private int successCount;
	
	
	public TextAndColor(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);
		
		this.successCount = 0;
		
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

	public void success() {
		this.successCount++;
	}
	
	public int calculateScore(){
		int score = (this.successCount - count)*20;
		if(score > 0)
			return score;
		else
			return 0;
	}
	
	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
		successCount = 0;
	}
	
	public void reset(){
		successCount = 0;
	}
	
	
	
}
