package com.g6pis.beatit.datatypes;

import com.g6pis.beatit.entities.State;

public class DTState {
	private String challengeId;
	private String challengeName;
	private String challengeDescription;
	private int challengeLevel;
	private int challengeDuration;
	private boolean finished;
	private double score;
	private DTDateTime dateTimeStart;
	private int currentAttempt;
	
	public DTState(State state) {
		challengeId = state.getChallenge().getChallengeId();
		challengeName = state.getChallenge().getName();
		challengeDescription = state.getChallenge().getDescription();
		challengeLevel = state.getChallenge().getLevel();
		challengeDuration = state.getChallenge().getDuration();
		finished = state.isFinished();
		score = state.getScore();
		dateTimeStart = state.getDateTimeStart();
		currentAttempt = state.getCurrentAttempt();
	}

	public String getChallengeId() {
		return challengeId;
	}

	public String getChallengeName() {
		return challengeName;
	}


	public String getChallengeDescription() {
		return challengeDescription;
	}

	public int getChallengeLevel() {
		return challengeLevel;
	}

	public int getChallengeDuration() {
		return challengeDuration;
	}

	public boolean isFinished() {
		return finished;
	}

	public double getScore() {
		return score;
	}


	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}

	public int getCurrentAttempt() {
		return currentAttempt;
	}

	
	
	
	
}
