package com.g6pis.beatitapp.interfaces;

import java.util.List;
import java.util.Map;

import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTRanking;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.entities.Round;
import com.g6pis.beatitapp.entities.State;
import com.g6pis.beatitapp.entities.User;

public interface IDataManager {
	

	public Round getCurrentRound();
	public void setCurrentRound(Round currentRound);
	public Map<String, State> getStates();
	public void setStates(Map<String, DTState> states);
	public User getUser();
	public double getScoreToSend();
	public List<DTState> getChallenges();
	public List<DTRanking> getRanking();
	public String login(String userId, String fbId, String firstName,
			String lastName, String country, String imageURL, boolean haveToSendScore);
	public void logout();
	public void updateRanking();
	public void saveScore(String challengeId, double score);
	public void sendScore();
	public DTState getState(String challengeId);
	public Challenge getChallenge(String challengeId);
	public Map<String, DTState> getPersistedStates();
	public String getPersistedRoundId();
	public boolean getHaveToSendScore();
}
