package com.g6pis.beatit.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.g6pis.beatit.datatypes.DTRanking;
import com.g6pis.beatit.datatypes.DTState;
import com.g6pis.beatit.entities.Round;
import com.g6pis.beatit.entities.State;
import com.g6pis.beatit.entities.User;

public class DataManager {

	
	
	private static DataManager instance = new DataManager();

	private User user;
	private Round actualRound;
	private Map<String, State> states;
	private boolean isLogged;
	private List<DTRanking> ranking;

	private DataManager() {
	}

	public static DataManager getInstance() {
		return instance;
	}

	public Round getActualRound() {
		return actualRound;
	}

	public void setActualRound(Round actualRound) {
		this.actualRound = actualRound;
	}

	public Map<String, State> getStates() {
		return states;
	}

	public void setStates(Map<String, State> states) {
		this.states = states;
	}

	public User getUser() {
		return user;
	}

	public List<DTState> getChallenges() {
		List<DTState> challenges = new ArrayList<DTState>();
		for (Map.Entry<String, State> entry : states.entrySet()) {
			challenges.add(new DTState(entry.getValue()));
		}

		return challenges;

	}

	public List<DTRanking> getRanking() {
		return ranking;
	}

	public void login(String fbId, String firstName, String lastName,
			String country) {

		// http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/
		// String url =
		// "http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/";

		try {
			LoginClass login = (LoginClass) new LoginClass().execute(fbId,
					firstName, lastName);
			String userId = login.get();
			user = new User(userId, fbId, firstName, lastName, country);
			
			RoundClass roundClass = (RoundClass) new RoundClass().execute();
			String round = roundClass.get();
			
			//TODO states
			
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		
		
	}

	public void logout() {
		user = null;
	}
	
	public List<DTRanking> updateRanking(){
		//TODO pedir ranking al servidor
		
		return ranking;
	}

}
