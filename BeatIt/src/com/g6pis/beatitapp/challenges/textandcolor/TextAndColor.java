package com.g6pis.beatitapp.challenges.textandcolor;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class TextAndColor extends Challenge {
	private static final String CHALLENGE_ID = "8";
	
	private static final int TIME_LEVEL1 = 60000;
	private static final int TIME_LEVEL2 = 45000;
	private static final int MIN_COUNT_LEVEL1 = 1;
	private static final int MIN_COUNT_LEVEL2 = 5;
	private static final int MIN_COUNT_LEVEL3 = 10;
	
	
	
	private int time;
	private int count;
	
	
	
	public TextAndColor(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);
		
		this.count = 0;
		
		switch(level){
		case 1:
			this.time = TIME_LEVEL1;
			break;
		case 2:
			this.time = TIME_LEVEL2;
			break;
		}
		
	}

	public int getTime() {
		return time;
	}
	
	public void addCount(){
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	public int calculateScore(){
		int score = 0;
		for(int i = 0; i < count; i++){
			if(i<=5)
				score += MIN_COUNT_LEVEL1;
			else if(i<= 25)
				score += MIN_COUNT_LEVEL2;
				else
					score += MIN_COUNT_LEVEL3;
				
		}
		return score;
	}
	
	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
		
		reset();
	}
	
	public void reset(){
		count = 0;
	}
	
	
	
}
