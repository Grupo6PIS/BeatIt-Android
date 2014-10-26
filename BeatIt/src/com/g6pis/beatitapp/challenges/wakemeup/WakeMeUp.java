package com.g6pis.beatitapp.challenges.wakemeup;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class WakeMeUp extends Challenge {
	private static final String CHALLENGE_ID = "2";
	
	private static final Integer NUMBER_OF_REPETITIONS_LEVEL1 = 3;
	private static final Integer NUMBER_OF_REPETITIONS_LEVEL2 = 4;
	private static final long TIME_LEVEL1_3 = 3;
	private static final long TIME_LEVEL2_4 = 3;

	private long succeed_times;
	private Integer number_of_repetitions;
	private long hidden_secs = 0; // This value can be modified in order to change the challenge difficulty
	
		
	public WakeMeUp(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name,level, maxAttempt, color);

		this.succeed_times = 0;
		
		switch (level) {
			case 1: {
				this.number_of_repetitions = NUMBER_OF_REPETITIONS_LEVEL1;
				this.hidden_secs = TIME_LEVEL1_3;
			}
			break;
			case 2: {
				this.number_of_repetitions = NUMBER_OF_REPETITIONS_LEVEL2;
				this.hidden_secs = TIME_LEVEL2_4;
			}
			break;
		}
			
	}

	public long getSucceed_times() {
		return succeed_times;
	}


	public void setSucceed_times(long succeed_times) {
		this.succeed_times = succeed_times;
	}

	public Integer getNumber_of_repetitions() {
		return number_of_repetitions;
	}

	public void setNumber_of_repetitions(Integer number_of_repetitions) {
		this.number_of_repetitions = number_of_repetitions;
	}

	public long getHidden_secs() {
		return hidden_secs;
	}

	public void setHidden_secs(long hidden_secs) {
		this.hidden_secs = hidden_secs;
	}
	

	public void reset(){
		this.succeed_times = 0;
	}

	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
	}

	//@Override
	public double calculateScore(){
		return (succeed_times)*80;
	}
		
}
