package com.g6pis.beatitapp.challenges.invitefriends;

import java.util.ArrayList;
import java.util.List;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.entities.Challenge;

public class CanYouPlay extends Challenge {
	private static final String CHALLENGE_ID = "3";
	
	private static final int MIN_FB_POST_LEVEL1 = 1;
	private static final int MIN_FB_POST_LEVEL2 = 1;
	private static final int MIN_SMS_LEVEL1 = 2;
	private static final int MIN_SMS_LEVEL2 = 4;
	

	private int minFBPost;
	private int minSMS;
	private List<String> phones;
	private int smsSent;
	private int fbPost;

	public CanYouPlay(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name, level, maxAttempt);

		fbPost = 0;
		smsSent = 0;

		switch (level) {

		case 1: {
			minFBPost = MIN_FB_POST_LEVEL1;
			minSMS = MIN_SMS_LEVEL1;
		}
			break;
		case 2: {
			minFBPost = MIN_FB_POST_LEVEL2;
			minSMS = MIN_SMS_LEVEL2;
		}
			break;
		}
		
		phones = new ArrayList<String>();
	}
	
	public int getMinFBPost() {
		return minFBPost;
	}

	public void setMinFBPost(int minFBPost) {
		this.minFBPost = minFBPost;
	}

	public int getMinSMS() {
		return minSMS;
	}

	public void setMinSMS(int minSMS) {
		this.minSMS = minSMS;
	}
	
	public List<String> getPhones() {
		return phones;
	}
	
	public boolean addPhone(String phone){
		if(!phones.contains(phone)){
			phones.add(phone);
			return true;
		}
		return false;
	}
	
	public void smsSent(){
		smsSent++;
	}
	
	public void fbPost(){
		fbPost++;
	}
	
	public double calculateScore() {
		if((fbPost >= minFBPost)&&(smsSent >= minSMS))
			return (fbPost + (smsSent*3))*10;
		
		return 0;
	}
	
	
	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
		phones = new ArrayList<String>();
		smsSent = 0;
		fbPost = 0;
	}
	
	public boolean isCompleted(){
		return ((fbPost == minFBPost)&&(smsSent == minSMS));
	}
	
	public void reset(){
		phones = new ArrayList<String>();
		smsSent = 0;
		fbPost = 0;
	}
}
