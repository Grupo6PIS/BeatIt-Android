package com.g6pis.beatitapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGame;
import com.g6pis.beatitapp.challenges.catchme.CatchMe;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlay;
import com.g6pis.beatitapp.challenges.selfiegroup.SelfieGroup;
import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDog;
import com.g6pis.beatitapp.challenges.songcomplete.SongComplete;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColor;
import com.g6pis.beatitapp.challenges.throwthephone.ThrowThePhone;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBolt;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUp;
import com.g6pis.beatitapp.connection.LoginConnection;
import com.g6pis.beatitapp.connection.RankingConnection;
import com.g6pis.beatitapp.connection.RoundConnection;
import com.g6pis.beatitapp.connection.ScoreConnection;
import com.g6pis.beatitapp.connection.StateConnection;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTRanking;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.entities.Round;
import com.g6pis.beatitapp.entities.State;
import com.g6pis.beatitapp.entities.User;
import com.g6pis.beatitapp.interfaces.IDataManager;

public class DataManager implements IDataManager {

	private static DataManager instance = new DataManager();

	private User user;
	private Round currentRound;
	private Map<String, State> states;
	private Map<String, DTState> persistedStates;
	private List<DTRanking> ranking;
	private double scoreToSend;
	private boolean haveToSendScore;

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

	public void setStates(Map<String, DTState> states) {
		this.persistedStates = states;
	}

	public User getUser() {
		return user;
	}

	public double getScoreToSend() {
		return scoreToSend;
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
			String lastName, String country, String imageURL, boolean haveToSendScore) {

		try {
			LoginConnection login = (LoginConnection) new LoginConnection()
					.execute(userId, fbId, firstName, lastName, imageURL);
			JSONObject json = login.get();
			JSONArray jsonStates = new JSONArray();
			JSONArray jsonStatesArr = new JSONArray();
			if(!json.getBoolean("error")){
				JSONObject jsonUser = json.getJSONObject("user");
				userId = jsonUser.getString("_id");
				Object obj = jsonUser.get("roundState");
				if(obj instanceof JSONObject)
					jsonStates = ((JSONObject)obj).getJSONArray("challenges");
				else
					jsonStatesArr = ((JSONArray)obj);
			
			user = new User(userId, fbId, firstName, lastName, country,
					imageURL);
			}

			// Server getRound
			RoundConnection roundConnection = (RoundConnection) new RoundConnection()
					.execute();
			JSONObject round = roundConnection.get();

			String roundId = round.getString("weekNumber");
			String startDate = round.getString("start_date");
			DTDateTime dateTimeStart = new DTDateTime(Long.parseLong(startDate));
			String endDate = round.getString("end_date");
			DTDateTime dateTimeFinish = new DTDateTime(Long.parseLong(endDate));
			long finishSeconds =  Long.parseLong(endDate)/1000;

			// Round Challenges
			JSONArray jsonChallenges = round.getJSONArray("challengeList");
			List<Challenge> challenges = new ArrayList<Challenge>();
			for (int i = 0; i < jsonChallenges.length(); i++) {
				JSONObject jsonChallenge = jsonChallenges.getJSONObject(i);
				String challengeId = jsonChallenge.getString("id");
				String name = jsonChallenge.getString("challengeName");
				int level = jsonChallenge.getInt("challengeLevel");
				String color = jsonChallenge.getString("colorHex");
				int maxAttempts = jsonChallenge.getInt("maxAttemps");
				switch (Integer.parseInt(challengeId)) {
				case 1: {
					UsainBolt usainBolt = new UsainBolt(challengeId, name,
							level, maxAttempts, color);
					challenges.add(usainBolt);
				}
					break;
				case 2: {
					WakeMeUp wakeMeUp = new WakeMeUp(challengeId, name, level, maxAttempts,color);
					challenges.add(wakeMeUp);
				}
					break;
				case 3: {
					CanYouPlay canYouPlay = new CanYouPlay(challengeId, name,
							level, maxAttempts, color);
					challenges.add(canYouPlay);
				}
					break;
				case 4: {
					ShutTheDog shutTheDog = new ShutTheDog(challengeId, name,
							level, maxAttempts, color);
					challenges.add(shutTheDog);

				}
					break;
				case 5: {
					BouncingGame bouncingGame = new BouncingGame(challengeId,
							name, level, maxAttempts, color);
					challenges.add(bouncingGame);
				}
					break;
				case 6: {
					ThrowThePhone throwThePhone = new ThrowThePhone(challengeId,
							name, level, maxAttempts, color);
					challenges.add(throwThePhone);
				}
				break;
				case 7: {
					CatchMe catchMe = new CatchMe(challengeId,
							name, level, maxAttempts, color);
					challenges.add(catchMe);
				}
				break;
				case 8: {
					TextAndColor textAndColor = new TextAndColor(challengeId,
							name, level, maxAttempts, color);
					challenges.add(textAndColor);
				}
					break;
				case 9: {
					SongComplete songComplete = new SongComplete(challengeId,
							name, level, maxAttempts, color);
					challenges.add(songComplete);
				}
				break;
				case 10:{
					SelfieGroup selfieGroup = new SelfieGroup(challengeId,
							name, level, maxAttempts, color);
					challenges.add(selfieGroup);
				}
				break;
				}
			}
//			SelfieGroup selfieGroup = new SelfieGroup("10",
//					"Selfie Group", 1, 3, "#FA6800");
//			challenges.add(selfieGroup);

			JSONArray jsonRanking = round.getJSONArray("ranking");
			ranking = new ArrayList<DTRanking>();
			for (int i = 0; i < jsonRanking.length(); i++) {
				JSONObject jsonRankingItem = jsonRanking.getJSONObject(i);
				String userName = jsonRankingItem.getString("userName");
				int score = jsonRankingItem.getInt("score");
				String rankingImageURL = jsonRankingItem.getString("imageURL");
				DTRanking rankingItem = new DTRanking(userName, score, i + 1,
						rankingImageURL);
				ranking.add(rankingItem);
			}

			currentRound = new Round(roundId, dateTimeStart, dateTimeFinish,
					challenges, finishSeconds);
			
			for(int i = 0; i< jsonStatesArr.length();i++){
				JSONObject jsonStatesObj = jsonStatesArr.getJSONObject(i);
				if(currentRound.getRoundId().equals(jsonStatesObj.getString("roundId"))){
					jsonStates = jsonStatesObj.getJSONArray("challenges");
				}
			}
			
			states = new HashMap<String, State>();
			String persistedRoundId = this.getPersistedRoundId();
			if (!persistedRoundId.equals(currentRound.getRoundId())) {
				persistedStates = new HashMap<String, DTState>();
				if((jsonStates != null) && jsonStates.length() > 0){
					for(int i = 0; i < jsonStates.length(); i++){
						JSONObject jsonState = jsonStates.getJSONObject(i);
						Challenge challenge = currentRound.getChallenge(jsonState.getString("challengeId"));
						int currentAttempt = currentRound.getChallenge(jsonState.getString("challengeId")).getMaxAttempt() - jsonState.getInt("attemps");
						State state = new State(currentRound,challenge , user,
												jsonState.getInt("bestScore"), 
												jsonState.getInt("lastScore"),
												currentAttempt);
						
						states.put(challenge.getChallengeId(), state);
						DTState dtState = new DTState(state);
						persistedStates.put(challenge.getChallengeId(), dtState);
					}
					if(jsonStates.length() != currentRound.getChallenges().size()){
						for (Challenge challenge : currentRound.getChallenges()) {
							if(!states.containsKey(challenge.getChallengeId())){
								State state = new State(currentRound, challenge, user);
								states.put(challenge.getChallengeId(), state);
								DTState dtState = new DTState(state);
								persistedStates.put(challenge.getChallengeId(), dtState);
							}
						}
					}
				}else{
					for (Challenge challenge : currentRound.getChallenges()) {
						State state = new State(currentRound, challenge, user);
						states.put(challenge.getChallengeId(), state);
						DTState dtState = new DTState(state);
						persistedStates.put(challenge.getChallengeId(), dtState);
					}
				}
			} else {
				for (Challenge challenge : currentRound.getChallenges()) {
					State state = new State(currentRound, challenge, user, persistedStates.get(
									challenge.getChallengeId()).getMaxScore(),
							persistedStates.get(challenge.getChallengeId())
									.getLastScore(), persistedStates.get(
									challenge.getChallengeId())
									.getCurrentAttempt());
					states.put(challenge.getChallengeId(), state);
					DTState dtState = new DTState(state);
					persistedStates.put(challenge.getChallengeId(), dtState);
				}

			}
			
			this.haveToSendScore = haveToSendScore;
			if (this.haveToSendScore) {
				int scoreToSend = 0;

				for (Map.Entry<String, State> entry : states.entrySet()) {
					scoreToSend += entry.getValue().getMaxScore();
				}
				sendScore(scoreToSend);
			}

		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return userId;
	}

	public void logout() {
			if (haveToSendScore) {
				int scoreToSend = 0;

				for (Map.Entry<String, State> entry : states.entrySet()) {
					scoreToSend += entry.getValue().getMaxScore();
				}
				sendScore(scoreToSend);
			}
			
			for (Map.Entry<String, State> entry : states.entrySet()) {
				
				String userId = entry.getValue().getUser().getUserId();
				String roundId = entry.getValue().getRound().getRoundId();
				String challengeId = entry.getValue().getChallenge().getChallengeId();
				String attempts = Integer.toString(entry.getValue().getChallenge().getMaxAttempt() - entry.getValue().getCurrentAttempt());
				String finished = Boolean.toString(entry.getValue().isFinished());
				String bestScore = Double.toString(entry.getValue().getMaxScore());
				String lastScore = Double.toString(entry.getValue().getLastScore());
			try {	
				StateConnection scoreConnection = (StateConnection) new StateConnection()
						.execute(userId,roundId,challengeId,attempts,finished,bestScore,lastScore);
				JSONObject response = scoreConnection.get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
		}
		}
		user = null;
		persistedStates = new HashMap<String, DTState>();
	}

	public void updateRanking() {
		try {
			RankingConnection rankingConnection = (RankingConnection) new RankingConnection()
					.execute();
			JSONArray jsonConnection = rankingConnection.get();
			JSONObject json = jsonConnection.getJSONObject(0);
			JSONArray jsonRanking = json.getJSONArray("ranking");
			ranking = new ArrayList<DTRanking>();
			for (int i = 0; i < jsonRanking.length(); i++) {
				JSONObject jsonRankingItem = jsonRanking.getJSONObject(i);
				String userName = jsonRankingItem.getString("userName");
				int score = jsonRankingItem.getInt("score");
				String rankingImageURL = jsonRankingItem.getString("imageURL");
				DTRanking rankingItem = new DTRanking(userName, score, i + 1,
						rankingImageURL);
				ranking.add(rankingItem);
			}

		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		} catch (JSONException e) {
		}

	}

	public void saveScore(String challengeId, double score) {
		State state = states.get(challengeId);
		if ((state.setNewScore(score))||(state.getCurrentAttempt() == 1)) {

			haveToSendScore = true;
			for (Map.Entry<String, State> entry : states.entrySet()) {
				if (entry.getValue().getCurrentAttempt() < 1)
					haveToSendScore = false;
			}
			if (haveToSendScore) {
				int scoreToSend = 0;

				for (Map.Entry<String, State> entry : states.entrySet()) {
					scoreToSend += entry.getValue().getMaxScore();
				}
				sendScore(scoreToSend);
				updateRanking();
			}
		}
	}

	public void sendScore(double score) {
		try {
			ScoreConnection scoreConnection = (ScoreConnection) new ScoreConnection()
					.execute(user.getUserId(), Double.toString(score), currentRound.getRoundId());
			JSONObject response = scoreConnection.get();
			if(response.has("error")){
				if((!response.getBoolean("error")))
					haveToSendScore = false;
			}
			
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public DTState getState(String challengeId) {
		DTState state = new DTState(states.get(challengeId));

		return state;
	}

	public Challenge getChallenge(String challengeId) {
		return currentRound.getChallenge(challengeId);
	}

	public Map<String, DTState> getPersistedStates() {
		return persistedStates;
	}

	public String getPersistedRoundId() {
		if (!persistedStates.isEmpty()) {
			Map.Entry<String, DTState> entry = persistedStates.entrySet()
					.iterator().next();

			String roundId = entry.getValue().getRoundId();

			return roundId;
		}
		return "";
	}
	
	public boolean getHaveToSendScore(){
		return haveToSendScore;
	}

}
