package com.g6pis.beatitapp.challenges.bouncinggame;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class BouncingGame extends Challenge {

	private static final String CHALLENGE_ID 		= "5";
	private static final long INITIAL_COUNTER_VALUE = 45000; // In milliseconds
	
	private int collision_times;
	private long time;
	private float decrease_radius_rate;
	
	public BouncingGame(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);

			switch (level) {

			case 1: {
				this.collision_times = 0;
				this.decrease_radius_rate = (float) 0.9;
			}
				break;
			case 2: {
				this.collision_times = 0;
				this.decrease_radius_rate = (float) 0.7;
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
		return (collision_times)*5;
	}

	public float getDecrease_radius_rate() {
		return decrease_radius_rate;
	}

	public void setDecrease_radius_rate(float decrease_radius_rate) {
		this.decrease_radius_rate = decrease_radius_rate;
	}
	
}
