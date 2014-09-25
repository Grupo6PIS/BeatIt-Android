package com.g6pis.beatit.challenges.usainbolt;

import java.util.HashSet;
import java.util.Set;

import com.g6pis.beatit.entities.Challenge;

public class UsainBolt extends Challenge {
	private double maxSpeed;
	private Set<Double> speeds;
	private double avgSpeed;

	public UsainBolt(String challengeId, String name, String description,
			Integer duration, Integer level, int maxAttempt) {
		super(challengeId, name, description, duration, level, maxAttempt);

		this.maxSpeed = 0;
		this.avgSpeed = 0;
		this.speeds = new HashSet<Double>();
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
