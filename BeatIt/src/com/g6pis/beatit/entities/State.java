package com.g6pis.beatit.entities;

import com.g6pis.beatit.datatypes.DTDateTime;


public class State {
	
	private boolean finished;
	private Integer score;
	private DTDateTime dateTimeStart;
	private int currentAttempt;
	
	public State(){
		
	}
	
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
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
