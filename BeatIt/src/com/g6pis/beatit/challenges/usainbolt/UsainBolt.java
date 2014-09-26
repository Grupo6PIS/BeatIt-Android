package com.g6pis.beatit.challenges.usainbolt;

import java.util.HashSet;
import java.util.Set;

import android.widget.TextView;

import com.g6pis.beatit.R;
import com.g6pis.beatit.entities.Challenge;

public class UsainBolt extends Challenge {
	private static final double MIN_SPEED_LEVEL1 = 10.0;
	private static final double MIN_SPEED_LEVEL2 = 15.0;
	private static final long TIME_LEVEL1 = 30000;
	private static final long TIME_LEVEL2 = 45000;
	
	private double minSpeed;
	private long time;
	
	private double maxSpeed;
	private Set<Double> speeds;
	private double avgSpeed;

	public UsainBolt(String challengeId, String name, String description,
			Integer duration, Integer level, int maxAttempt) {
		super(challengeId, name, description, duration, level, maxAttempt);

		this.maxSpeed = 0;
		this.avgSpeed = 0;
		this.speeds = new HashSet<Double>();
		
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

	public Set<Double> getSpeeds() {
		return speeds;
	}

	public void setSpeeds(Set<Double> speeds) {
		this.speeds = speeds;
	}

	public double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	
	
	public void addSpeed(double speed){
		speeds.add(speed);
		
		if(speed > maxSpeed){
			maxSpeed = speed;
		}
	}
	
	//@Override
	public double calculateScore(){
		// Calculating the average speed
		avgSpeed = 0.0;
		for (Double speed : speeds) {
			avgSpeed += speed;
		}
		avgSpeed /= this.speeds.size();
		
		return (maxSpeed + avgSpeed)*2;
	}
	
}
