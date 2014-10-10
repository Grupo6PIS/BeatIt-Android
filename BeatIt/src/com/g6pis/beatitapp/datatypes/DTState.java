package com.g6pis.beatitapp.datatypes;

import com.g6pis.beatitapp.entities.State;

public class DTState {
	private String challengeId;
	private String roundId;
	private String challengeName;
	private String challengeDescription;
	private int challengeLevel;
	private int challengeDuration;
	private boolean finished;
	private double maxScore;
	private double lastScore;
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	private int currentAttempt;
	private DTDateTime lastFinishDateTime;

	public DTState(State state) {
		challengeId = state.getChallenge().getChallengeId();
		roundId = state.getRound().getRoundId();
		challengeName = state.getChallenge().getName();
		challengeLevel = state.getChallenge().getLevel();
		finished = state.isFinished();
		maxScore = state.getMaxScore();
		lastScore = state.getLastScore();
		dateTimeStart = state.getRound().getDateTimeStart();
		dateTimeFinish = state.getRound().getDateTimeFinish();
		currentAttempt = state.getCurrentAttempt();
		lastFinishDateTime = state.getLastFinishDateTime();
	}

	public DTState(String challengeId, String roundId, double maxScore, double lastScore,
			int currentAttempt, boolean finished, DTDateTime lastFinishDateTime) {
		super();
		this.challengeId = challengeId;
		this.roundId = roundId;
		this.finished = finished;
		this.maxScore = maxScore;
		this.lastScore = lastScore;
		this.currentAttempt = currentAttempt;
		this.lastFinishDateTime = lastFinishDateTime;
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

	public double getMaxScore() {
		return maxScore;
	}

	public double getLastScore() {
		return lastScore;
	}

	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}

	public int getCurrentAttempt() {
		return currentAttempt;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public void setChallengeDescription(String challengeDescription) {
		this.challengeDescription = challengeDescription;
	}

	public void setChallengeLevel(int challengeLevel) {
		this.challengeLevel = challengeLevel;
	}

	public void setChallengeDuration(int challengeDuration) {
		this.challengeDuration = challengeDuration;
	}

	public DTDateTime getDateTimeFinish() {
		return dateTimeFinish;
	}

	public DTDateTime getLastFinishDateTime() {
		return lastFinishDateTime;
	}
	
	
}
