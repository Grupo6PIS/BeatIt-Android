package com.g6pis.beatit.entities;

import android.app.Activity;

public class Challenge extends Activity{
	
	private int challengeId;
	private String name;
	private String description;
	private Integer duration;
	private Integer level;
	private int maxAttempt;
	
	public int getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(int challengeId) {
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
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getLevel() {
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
	
	

}
