package com.g6pis.beatit.entities;

import com.g6pis.beatit.classes.User;
import com.g6pis.beatit.datatypes.DTDateTime;


public class State {
	
	private Round round;
	private Challenge challenge;
	private User user;
	private boolean finished;
	private double score;
	private DTDateTime dateTimeStart;
	private int currentAttempt;
	
	public State(Round round, Challenge challenge, User user){
		this.round = round;
		this.challenge = challenge;
		this.user = user;
		this.finished = false;
		this.score = 0;
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

	public void setScore(double score) {
		this.score = score;
	}
	
	public double getScore() {
		if(!finished)
			return 0;
		
		return score;
		
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


	

}
