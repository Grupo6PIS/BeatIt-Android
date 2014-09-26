package com.g6pis.beatit.challenges.invitefriends;

import java.util.ArrayList;
import java.util.List;

import com.g6pis.beatit.entities.Challenge;

public class CanYouPlay extends Challenge {
	private static final int MIN_FB_POST_LEVEL1 = 3;
	private static final int MIN_FB_POST_LEVEL2 = 5;
	private static final int MIN_SMS_LEVEL1 = 2;
	private static final int MIN_SMS_LEVEL2 = 4;

	private int minFBPost;
	private int minSMS;
	private List<String> phones;
	private int smsSent;
	private int fbPost;

	public CanYouPlay(String challengeId, String name, String description,
			Integer duration, Integer level, int maxAttempt) {
		super(challengeId, name, description, duration, level, maxAttempt);

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
		return (fbPost + (smsSent*3))*10;
	}

}
