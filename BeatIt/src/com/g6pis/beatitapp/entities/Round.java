package com.g6pis.beatitapp.entities;

import java.util.List;
import java.util.Map;

import com.g6pis.beatitapp.datatypes.DTDateTime;

public class Round {

	private String roundId;
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	private List<Challenge> challenges;
	private long finishSeconds;

	public Round(String roundId, DTDateTime dateTimeStart,
			DTDateTime dateTimeFinish, List<Challenge> challenges,
			long finishSeconds) {
		super();
		this.roundId = roundId;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeFinish = dateTimeFinish;
		this.challenges = challenges;
		this.finishSeconds = finishSeconds;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}

	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	public DTDateTime getDateTimeFinish() {
		return dateTimeFinish;
	}

	public void setDateTimeFinish(DTDateTime dateTimeFinish) {
		this.dateTimeFinish = dateTimeFinish;
	}

	public List<Challenge> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<Challenge> challenges) {
		this.challenges = challenges;
	}

	public Challenge getChallenge(String challengeId) {
		Challenge challenge = null;
		int index = 0;

		while ((challenge == null) && index < challenges.size()) {
			if (challenges.get(index).getChallengeId().equals(challengeId))
				challenge = challenges.get(index);

			index++;
		}

		return challenge;
	}

	public long getFinishSeconds() {
		return finishSeconds;
	}

	public void setFinishSeconds(long finishSeconds) {
		this.finishSeconds = finishSeconds;
	}

}
