package com.g6pis.beatit.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

import com.g6pis.beatit.datatypes.DTRanking;
import com.g6pis.beatit.datatypes.DTState;
import com.g6pis.beatit.entities.Round;
import com.g6pis.beatit.entities.State;
import com.g6pis.beatit.entities.User;

//TODO
public class DataManager {

	private static DataManager instance = new DataManager();

	private User user;
	private Round actualRound;
	private Map<String, State> states;
	private boolean isLogged;

	private DataManager() {

		// TODO agarrar los states de la base o crearlos.
		// TODO pedir la ronda actual al servidor.
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
		List<DTRanking> ranking = new ArrayList<DTRanking>();

		// TODO pedir al servidor el ranking

		return ranking;
	}

	public void login(String fbId, String firstName, String lastName,
			String country) {

		// http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/
		//String url = "http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/";
		
		try {
			LoginClass login = (LoginClass) new LoginClass().execute(fbId,firstName,lastName);
			String userId = login.get();
			user = new User(userId, fbId, firstName, lastName, country);
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		

	}

	public void logout() {
		user = null;
	}

}
