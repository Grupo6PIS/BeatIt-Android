package com.g6pis.beatitapp.challenges.usainbolt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.widget.TextView;


import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

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
	private double speeds;
	private int speedCount;

	public UsainBolt(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name, level, maxAttempt);

		this.maxSpeed = 0.0;
		this.avgSpeed = 0.0;
		this.speeds = 0.0;
		this.speedCount = 0;
		
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
		speeds += speed;
		speedCount++;
		
		if(speed > maxSpeed){
			maxSpeed = speed;
		}
	}
	
	public double calculateScore(){
		avgSpeed = speeds/speedCount;
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
	
	public void reset(){
		this.maxSpeed = 0.0;
		this.avgSpeed = 0.0;
	}
	
}
