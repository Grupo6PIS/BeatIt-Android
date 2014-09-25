package com.g6pis.beatit.entities;

import android.app.Activity;

public  class Challenge{
	
	private String challengeId;
	private String name;
	private String description;
	private int duration;
	private int level;
	private int maxAttempt;
	
	
	
	protected Challenge(String challengeId, String name, String description,
			Integer duration, Integer level, int maxAttempt) {
		this.challengeId = challengeId;
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.level = level;
		this.maxAttempt = maxAttempt;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
	
		return "Challenge [challengeId=" + this.getChallengeId() + ", name=" + this.getName() +
				", description=" + this.getDescription() + ", duration=" + this.getDuration() + ", level=" + this.getLevel() + "]";
				
		
	}
	public int getMaxAttempt() {
		return maxAttempt;
	}
	public void setMaxAttempt(int maxAttempt) {
		this.maxAttempt = maxAttempt;
	}
	
	//protected abstract double calculateScore();
	

}
