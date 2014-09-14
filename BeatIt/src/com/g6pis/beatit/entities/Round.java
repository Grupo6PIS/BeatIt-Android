package com.g6pis.beatit.entities;

import com.g6pis.beatit.datatypes.DTDateTime;

public class Round {
	
	private String roundId;
	private DTDateTime dateTimeStart;
	private DTDateTime dateTimeFinish;
	
	
	public Round(String roundId, DTDateTime dateTimeStart,
			DTDateTime dateTimeFinish) {
		super();
		this.roundId = roundId;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeFinish = dateTimeFinish;
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
	
	

}
