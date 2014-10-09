package com.g6pis.beatit.challenges.usainbolt;

import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.entities.Challenge;

public class UsainBolt extends Challenge {
	private static final String CHALLENGE_ID 		= "1";
	
	private static final double MIN_SPEED_LEVEL1 = 10.0;
	private static final double MIN_SPEED_LEVEL2 = 15.0;
	private static final long TIME_LEVEL1 = 30000;
	private static final long TIME_LEVEL2 = 45000;
	
	private double minSpeed;
	private long time;
	
	private double maxSpeed;
	private double avgSpeed;

	public UsainBolt(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name, level, maxAttempt);

		this.maxSpeed = 0.0;
		this.avgSpeed = 0.0;
		
		switch (level) {

		case 1: {
			minSpeed = MIN_SPEED_LEVEL1;
			time = TIME_LEVEL1;
		}
			break;
		case 2: {
			minSpeed = MIN_SPEED_LEVEL2;
			time = TIME_LEVEL2;
		}
			break;
		}
	
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	
	public void addSpeed(double speed){
		avgSpeed = (avgSpeed+speed)/2;
		
		if(speed > maxSpeed){
			maxSpeed = speed;
		}
	}
	
	public double calculateScore(){	
		return (maxSpeed + avgSpeed)*2;
	}
	
	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
		this.maxSpeed = 0.0;
		this.avgSpeed = 0.0;
	}

	public double getMinSpeed() {
		return minSpeed;
	}

	public long getTime() {
		return time;
	}
	
	
}
