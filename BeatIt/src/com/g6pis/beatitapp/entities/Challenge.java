package com.g6pis.beatitapp.entities;

import android.app.Activity;

public  class Challenge{
	
	private String challengeId;
	private String name;
	private int level;
	private int maxAttempt;
	private String color;
	
	
	
	protected Challenge(String challengeId, String name, Integer level, int maxAttempt, String color) {
		this.challengeId = challengeId;
		this.name = name;
		this.level = level;
		this.maxAttempt = maxAttempt;
		this.color = color;
	}
	
	
	public String getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public int getMaxAttempt() {
		return maxAttempt;
	}
	public void setMaxAttempt(int maxAttempt) {
		this.maxAttempt = maxAttempt;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	

}
