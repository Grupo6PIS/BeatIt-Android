package com.g6pis.beatitapp.challenges.selfiegroup;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class SelfieGroup extends Challenge {
	private final int MIN_PEOPLE_1 = 2;
	private final int MIN_PEOPLE_2 = 5;
	
	private final String CHALLENGE_ID = "10";

	private int minPeople;
	private int people;

	public SelfieGroup(String challengeId, String name, Integer level,
			int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);

		people = 0;

		switch (level) {
		case 1:
			minPeople = MIN_PEOPLE_1;
			break;
		case 2:
			minPeople = MIN_PEOPLE_2;
			break;
		}
	}

	public int getMinPeople() {
		return minPeople;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}
	
	public int calculateScore(){
		if(people >= minPeople){
			return 60 + (people - minPeople)*(60/minPeople);
		}
		return 0;
	}
	
	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
		
		people = 0;
	}
	
	
	

}
