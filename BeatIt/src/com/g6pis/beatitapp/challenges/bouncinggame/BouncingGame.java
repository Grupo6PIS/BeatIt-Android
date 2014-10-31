package com.g6pis.beatitapp.challenges.bouncinggame;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class BouncingGame extends Challenge {

	private static final String CHALLENGE_ID 		= "5";
	private static final long INITIAL_COUNTER_VALUE = 60000; // In milliseconds
	
	private int collision_times;
	private long time;
	
	public BouncingGame(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name, level, 999,color);

			switch (level) {

			case 1: {
				this.collision_times = 0;
			}
				break;
			case 2: {
				this.collision_times = 0;
			}
				break;
			}
			
			this.time = INITIAL_COUNTER_VALUE;
		
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getCollision_times() {
		return collision_times;
	}

	public void increaseCollision_times() {
		this.collision_times = this.collision_times + 1;
	}

	public void setCollision_times(int collision_times) {
		this.collision_times = collision_times;
	}

	public void reset(){
		this.collision_times = 0;
	}

	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
	}

	//@Override
	public double calculateScore(){
		return (collision_times)*10;
	}
	
}
