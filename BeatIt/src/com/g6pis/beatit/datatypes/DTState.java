package com.g6pis.beatit.datatypes;

import com.g6pis.beatit.entities.State;

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
	private int currentAttempt;

	public DTState(State state) {
		challengeId = state.getChallenge().getChallengeId();
		roundId = state.getRound().getRoundId();
		challengeName = state.getChallenge().getName();
		challengeDescription = state.getChallenge().getDescription();
		challengeLevel = state.getChallenge().getLevel();
		challengeDuration = state.getChallenge().getDuration();
		finished = state.isFinished();
		maxScore = state.getMaxScore();
		lastScore = state.getLastScore();
		dateTimeStart = state.getDateTimeStart();
		currentAttempt = state.getCurrentAttempt();
	}

	public DTState(String challengeId, String roundId, double maxScore, double lastScore,
			int currentAttempt, boolean finished) {
		super();
		this.challengeId = challengeId;
		this.roundId = roundId;
		this.finished = finished;
		this.maxScore = maxScore;
		this.lastScore = lastScore;
		this.currentAttempt = currentAttempt;
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

	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

}
