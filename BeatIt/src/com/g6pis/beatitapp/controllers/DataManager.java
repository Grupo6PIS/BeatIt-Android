package com.g6pis.beatitapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDog;
import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGame;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlay;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColor;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBolt;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUp;
import com.g6pis.beatitapp.connection.LoginConnection;
import com.g6pis.beatitapp.connection.RankingConnection;
import com.g6pis.beatitapp.connection.RoundConnection;
import com.g6pis.beatitapp.connection.ScoreConnection;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.datatypes.DTRanking;
import com.g6pis.beatitapp.datatypes.DTState;
import com.g6pis.beatitapp.entities.Challenge;
import com.g6pis.beatitapp.entities.Round;
import com.g6pis.beatitapp.entities.State;
import com.g6pis.beatitapp.entities.User;

public class DataManager {

	private static DataManager instance = new DataManager();

	private User user;
	private Round currentRound;
	private Map<String, State> states;
	private Map<String, DTState> persistedStates;
	private List<DTRanking> ranking;
	private double scoreToSend;

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
			String lastName, String country, String imageURL) {

		try {
			LoginConnection login = (LoginConnection) new LoginConnection()
					.execute(userId, fbId, firstName, lastName, imageURL);
			JSONObject jsonUser = login.get();
			userId = jsonUser.getString("_id");

			user = new User(userId, fbId, firstName, lastName, country,
					imageURL);

			// Server getRound
			RoundConnection roundConnection = (RoundConnection) new RoundConnection()
					.execute();
			JSONObject round = roundConnection.get();

			String roundId = round.getString("weekNumber");
			String startDate = round.getString("start_date");
			DTDateTime dateTimeStart = new DTDateTime(Long.parseLong(startDate));
			String endDate = round.getString("end_date");
			DTDateTime dateTimeFinish = new DTDateTime(Long.parseLong(endDate));

			// Round Challenges
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
				}
					break;
				case 4: {
					ShutTheDog shutTheDog = new ShutTheDog(challengeId, name,
							level, 3);
					challenges.add(shutTheDog);

				}
					break;
				case 5: {
					BouncingGame bouncingGame = new BouncingGame(challengeId,
							name, level, 3);
					challenges.add(bouncingGame);
				}
					break;
				case 8: {
					TextAndColor textAndColor = new TextAndColor(challengeId,
							name, level, 3);
					challenges.add(textAndColor);
				}
					break;
				}
			}

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
					challenges);

			states = new HashMap<String, State>();
			String persistedRoundId = this.getPersistedRoundId();
			if (!persistedRoundId.equals(currentRound.getRoundId())) {
				persistedStates = new HashMap<String, DTState>();
				for (Challenge challenge : currentRound.getChallenges()) {
					State state = new State(currentRound, challenge, user);
					states.put(challenge.getChallengeId(), state);
					DTState dtState = new DTState(state);
					persistedStates.put(challenge.getChallengeId(), dtState);
				}
			} else {
				for (Challenge challenge : currentRound.getChallenges()) {
					State state = new State(currentRound, challenge, user,
							persistedStates.get(challenge.getChallengeId())
									.isFinished(), persistedStates.get(
									challenge.getChallengeId()).getMaxScore(),
							persistedStates.get(challenge.getChallengeId())
									.getLastScore(), persistedStates.get(
									challenge.getChallengeId())
									.getCurrentAttempt(), persistedStates.get(
									challenge.getChallengeId())
									.getLastFinishDateTime());

					states.put(challenge.getChallengeId(), state);
					DTState dtState = new DTState(state);
					persistedStates.put(challenge.getChallengeId(), dtState);
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
		if (state.setNewScore(score)) {

			boolean haveToSendScore = true;
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
					.execute(user.getUserId(), Double.toString(score));
			JSONObject response = scoreConnection.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
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

}
