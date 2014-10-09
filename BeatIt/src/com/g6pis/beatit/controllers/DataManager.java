package com.g6pis.beatit.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.g6pis.beatit.challenges.callaalperro.ShutTheDog;
import com.g6pis.beatit.challenges.despertameatiempo.WakeMeUp;
import com.g6pis.beatit.challenges.invitefriends.CanYouPlay;
import com.g6pis.beatit.challenges.usainbolt.UsainBolt;
import com.g6pis.beatit.connection.LoginConnection;
import com.g6pis.beatit.connection.RankingConnection;
import com.g6pis.beatit.connection.RoundConnection;
import com.g6pis.beatit.connection.ScoreConnection;
import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.datatypes.DTRanking;
import com.g6pis.beatit.datatypes.DTState;
import com.g6pis.beatit.entities.Challenge;
import com.g6pis.beatit.entities.Round;
import com.g6pis.beatit.entities.State;
import com.g6pis.beatit.entities.User;
import com.g6pis.beatit.persistence.StateDataSource;

public class DataManager {

	// TODO cada vez que se actualizan los states, se persisten

	private static DataManager instance = new DataManager();

	private User user;
	private Round currentRound;
	private Map<String, State> states;
	private Map<String,DTState> persistedStates;
	private boolean isLogged;
	private List<DTRanking> ranking;
	
	
	public static StateDataSource stateDao = null;
	public static SQLiteDatabase localDB = null;

	private static int mReferenceCount = 0;

	private DataManager() {
	}

	public static DataManager getInstance() {
		return instance;
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}

	public Map<String, State> getStates() {
		return states;
	}

	public void setStates(Map<String,DTState> states) {
		this.persistedStates = states;
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

	public String login(String userId, String fbId, String firstName,
			String lastName, String country, String imageURL) {

		try {
			LoginConnection login = (LoginConnection) new LoginConnection()
					.execute(userId, fbId, firstName, lastName, imageURL);
			JSONObject jsonUser = login.get();
			userId = jsonUser.getString("_id");

			user = new User(userId, fbId, firstName, lastName, country,
					imageURL);
			
			
			//Server getRound
			RoundConnection roundConnection = (RoundConnection) new RoundConnection()
					.execute();
			JSONObject round = roundConnection.get();

			String roundId = round.getString("weekNumber");
			String startDate = round.getString("start_date");
			DTDateTime dateTimeStart = new DTDateTime(Long.parseLong(startDate));
			String endDate = round.getString("end_date");
			DTDateTime dateTimeFinish = new DTDateTime(Long.parseLong(endDate));
			
			//Round Challenges
			JSONArray jsonChallenges = round.getJSONArray("challengeList");
			List<Challenge> challenges = new ArrayList<Challenge>();
			for (int i = 0; i < jsonChallenges.length(); i++) {
				JSONObject jsonChallenge = jsonChallenges.getJSONObject(i);
				String challengeId = jsonChallenge.getString("id");
				String name = jsonChallenge.getString("challengeName");
				int level = jsonChallenge.getInt("challengeLevel");
				switch (Integer.parseInt(challengeId)) {
				case 1: {
					UsainBolt usainBolt = new UsainBolt(challengeId, name,
							level, 3);
					challenges.add(usainBolt);
				}
					break;
				case 2: {
					WakeMeUp wakeMeUp = new WakeMeUp(challengeId, name, level,
							3);
					challenges.add(wakeMeUp);
				}
					break;
				case 3: {
					CanYouPlay canYouPlay = new CanYouPlay(challengeId, name,
							level, 3);
					challenges.add(canYouPlay);
				}break;
				case 4:{
					ShutTheDog shutTheDog = new ShutTheDog(challengeId,name,level,3);
					challenges.add(shutTheDog);

				}break;
				}
			}
			
			//TODO Verificar Ranking
			JSONArray jsonRanking = round.getJSONArray("ranking");
			ranking = new ArrayList<DTRanking>();
			for (int i = 0; i < jsonRanking.length(); i++) {
				JSONObject jsonRankingItem = jsonRanking.getJSONObject(i);
				String userName = jsonRankingItem.getString("userName");
				int score = jsonRankingItem.getInt("score");
				String rankingImageURL = jsonRankingItem.getString("imageURL");
				DTRanking rankingItem = new DTRanking(userName, score,i+1, rankingImageURL);
				ranking.add(rankingItem);
			}
			
			currentRound = new Round(roundId,dateTimeStart, dateTimeFinish, challenges);
			
			//TODO comparar la ronda de los states de la BD con la ronda actual,
			//si es distinta, se cambian
			states = new HashMap<String,State>();
			String persistedRoundId = this.getPersistedRoundId();
			if(!persistedRoundId.equals(currentRound.getRoundId())){
				persistedStates = new HashMap<String,DTState>();
				for(Challenge challenge : currentRound.getChallenges()){
					State state = new State(currentRound,challenge,user);
					states.put(challenge.getChallengeId(), state);
					DTState dtState = new DTState(state);
					persistedStates.put(challenge.getChallengeId(),dtState);
				}
			}else{
				for(Challenge challenge : currentRound.getChallenges()){
					State state = new State(currentRound,challenge,user);
					
					state.setCurrentAttempt(persistedStates.get(challenge.getChallengeId()).getCurrentAttempt());
					state.setFinished(persistedStates.get(challenge.getChallengeId()).isFinished());
					state.setLastScore(persistedStates.get(challenge.getChallengeId()).getLastScore());
					state.setMaxScore(persistedStates.get(challenge.getChallengeId()).getMaxScore());
					state.setLastFinishDateTime(persistedStates.get(challenge.getChallengeId()).getLastFinishDateTime());
					states.put(challenge.getChallengeId(), state); 
					DTState dtState = new DTState(state);
					persistedStates.put(challenge.getChallengeId(),dtState);
				}
				
			}
			

		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		} catch (JSONException e) {
		}
		
		return userId;
	}

	public void logout() {
		user = null;
		persistedStates = new HashMap<String,DTState>();
	}

	public List<DTRanking> updateRanking() {
		try {
			RankingConnection rankingConnection = (RankingConnection) new RankingConnection()
			.execute();
			JSONObject jsonRanking = rankingConnection.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return ranking;
	}

	public void saveScore(String challengeId, double score) {	
		State state  = states.get(challengeId);
		state.setNewScore(score);
		boolean haveToSendScore = true;
		for (Map.Entry<String, State> entry : states.entrySet()) {
			if(entry.getValue().getCurrentAttempt()<1)
				haveToSendScore = false;
		}
		if(haveToSendScore){
			sendScore(score);
		}
	}
	
	public void sendScore(double score){
		try {
			ScoreConnection scoreConnection = (ScoreConnection) new ScoreConnection()
			.execute(user.getUserId(),Double.toString(score));
			JSONObject response = scoreConnection.get();
			//TODO manejar si tiene error.
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
	}
	
	public DTState getState(String challengeId){
		DTState state = new DTState(states.get(challengeId));
		
		return state;
	}
	
	public Challenge getChallenge(String challengeId){
		return currentRound.getChallenge(challengeId);
	}

	public Map<String,DTState> getPersistedStates() {
		return persistedStates;
	}
	
	public String getPersistedRoundId(){
		if(!persistedStates.isEmpty()){
			Map.Entry<String, DTState> entry = persistedStates.entrySet().iterator().next();
		
			String roundId = entry.getValue().getRoundId();

			return roundId;
		}
		return "";
	}
	
	
	  /**
     * 
     * 
     * @return this
     * @throws SQLException
     * if the database could be neither opened or created
     */
    public StateDataSource open(Context context){
        if( mReferenceCount == 0 ) {

            if( stateDao == null )
            	stateDao = new StateDataSource(context);
          
            if( localDB == null || localDB.isOpen() == false )
            	localDB = stateDao.open();
        }
        mReferenceCount++;

        return stateDao;
    }
	
}
