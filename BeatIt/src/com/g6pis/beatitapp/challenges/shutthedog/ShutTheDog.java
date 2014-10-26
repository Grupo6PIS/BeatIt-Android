package com.g6pis.beatitapp.challenges.shutthedog;

import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.interfaces.Factory;

public class ShutTheDog extends Challenge{
	private static final String CHALLENGE_ID = "4";
	
	private static final int ARRAY_LENGTH_LEVEL1 = 3;
	private static final int ARRAY_LENGTH_LEVEL2 = 5;
	
	private int lives;
	private boolean hasWon;
	private int results[] = new int[3];
	private double score;
	int arrayLength = 3;
	
	public ShutTheDog(String challengeId, String name, Integer level, int maxAttempt, String color) {
		super(challengeId, name, level, maxAttempt, color);
		// TODO Auto-generated constructor stub
		lives = 3;
		hasWon = false;
		
		switch (level) {

		case 1: {
			arrayLength = ARRAY_LENGTH_LEVEL1;
		}
			break;
		case 2: {
			arrayLength = ARRAY_LENGTH_LEVEL2;
		}
			break;
		}
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getArrayLength() {
		return arrayLength;
	}

	public void setArrayLength(int arrayLength) {
		this.arrayLength = arrayLength;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public boolean isHasWon() {
		return hasWon;
	}

	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}

	public int[] getResults() {
		return results;
	}

	public void setResults(int[] results) {
		this.results = results;
	}

	public double calculateScore() {			
		score = 0;
        for (int i = 0; i < arrayLength; i++) 
        {
            if (results[i] > 250){
            	double agregado = (double) 100/ (double) results[i];
                score = score + agregado;
            }
        }
        return Math.floor(score * 10 * 100) / 10;
	}
	
	
	public void finishChallenge(){
		Factory.getInstance().getIDataManager().saveScore(CHALLENGE_ID, calculateScore());
		
		lives = 0;
		hasWon = false;
		results = new int[3];
	}
	
	public boolean isCompleted(){
		return hasWon;
	}
	
	public void reset(){
		/*phones = new ArrayList<String>();
		smsSent = 0;
		fbPost = 0;*/
	}

}
