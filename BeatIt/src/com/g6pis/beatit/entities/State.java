package com.g6pis.beatit.entities;

import com.g6pis.beatit.datatypes.DTDateTime;


public class State {
	
	private Round round;
	private Challenge challenge;
	private User user;
	private boolean finished;
	private double maxScore;
	private double lastScore;
	private DTDateTime dateTimeStart;
	private int currentAttempt;
	
	public State(Round round, Challenge challenge, User user){
		this.round = round;
		this.challenge = challenge;
		this.user = user;
		this.finished = false;
		this.maxScore = 0;
		this.lastScore = 0;
		this.currentAttempt = 0;
		this.dateTimeStart = new DTDateTime();
		
	}
	
	

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public Challenge getChallenge() {
		return challenge;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}
	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	public int getCurrentAttempt() {
		return currentAttempt;
	}

	public void setCurrentAttempt(int currentAttempt) {
		this.currentAttempt = currentAttempt;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public double getLastScore() {
		return lastScore;
	}

	public void setLastScore(double lastScore) {
		this.lastScore = lastScore;
		
		if(this.lastScore > this.maxScore)
			this.maxScore = this.lastScore;
		
		this.currentAttempt++;
		if(this.currentAttempt == this.challenge.getMaxAttempt()){
			this.finished = true;
		}
		
	}
}
