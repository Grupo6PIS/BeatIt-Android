package com.g6pis.beatit.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
	private Map<String,State> states;
	private boolean isLogged;
	private SharedPreferences sharedPrefs;

	
	
	private DataManager(){	
		
		//TODO agarrar los states de la base o crearlos.
		//TODO pedir la ronda actual al servidor.
	}
	
	public static DataManager getInstance(){
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
	
	public List<DTState> getChallenges(){
		List<DTState> challenges = new ArrayList<DTState>();
        for (Map.Entry<String, State> entry : states.entrySet()) {
            challenges.add(new DTState(entry.getValue()));
        }
        
        return challenges;
			
	}
	
	public List<DTRanking> getRanking(){
		List<DTRanking> ranking = new ArrayList<DTRanking>();
		
		//TODO pedir al servidor el ranking
		
		return ranking;
	}

	public void login(String fbId, String firstName, String lastName, String country){
		
		user = new User(/*userId,*/fbId,firstName,lastName,country);
		
		//agregar al servidor el user
		
	}
	
	public void logout(){
		user = null;		
	}
	
	

}
