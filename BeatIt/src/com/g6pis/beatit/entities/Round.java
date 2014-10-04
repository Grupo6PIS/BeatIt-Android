package com.g6pis.beatit.entities;

import java.util.List;
import java.util.Map;

import com.g6pis.beatit.datatypes.DTDateTime;

public class Round {
	
	private String roundId;
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	private List<Challenge> challenges;
	
	
	
	
	public Round(String roundId, DTDateTime dateTimeStart,
			DTDateTime dateTimeFinish, List<Challenge> challenges) {
		super();
		this.roundId = roundId;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeFinish = dateTimeFinish;
		this.challenges = challenges;
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


	
	

}
