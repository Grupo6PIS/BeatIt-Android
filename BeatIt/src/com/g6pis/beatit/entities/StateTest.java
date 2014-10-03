package com.g6pis.beatit.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.g6pis.beatit.datatypes.DTDateTime;

public class StateTest {

	@Test
	public void ConstructorTest() {
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		Map<String,Challenge> challenges = new HashMap<String,Challenge>();
		
		Challenge challenge = new Challenge("1", "Challenge 1", "Challenge 1 description", 7, 2, 3);
		challenges.put(challenge.getChallengeId(), challenge);
		
		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish, challenges);
		
		User user = new User("123", "456", "Johnnie", "Walker", "USA", "facebook.com");
		
		State state = new State(round, challenge, user);
		DTDateTime dateTime = state.getDateTimeStart();
		
		assertEquals(round, state.getRound());
		assertEquals(challenge, state.getChallenge());
		assertEquals(user, state.getUser());
		assertEquals(dateTime,state.getDateTimeStart());
		assertEquals(0,state.getCurrentAttempt());
		assertEquals(0,state.getMaxScore(),0);
		assertEquals(0,state.getLastScore(),0);
		assertFalse(state.isFinished());
	}
	
	@Test
	public void settersTest(){
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		Map<String,Challenge> challenges = new HashMap<String,Challenge>();
		
		Challenge challenge = new Challenge("1", "Challenge 1", "Challenge 1 description", 7, 2, 3);
		challenges.put(challenge.getChallengeId(), challenge);
		
		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish, challenges);
		
		User user = new User("123", "456", "Johnnie", "Walker", "USA", "facebook.com");
		
		State state = new State(round, challenge, user);
		
		challenges = new HashMap<String,Challenge>();
		
		challenge = new Challenge("2", "Challenge 2", "Challenge 2 description", 7, 2, 3);
		challenges.put(challenge.getChallengeId(), challenge);
		
		round = new Round("Round 2", dateTimeStart, dateTimeFinish, challenges);
		
		user = new User("789", "987", "William", "Lawson", "England", "facebook.com");
		
		
		DTDateTime dateTime = new DTDateTime(6, 3, 1992, 9, 30, 0);
		
		state.setChallenge(challenge);
		state.setCurrentAttempt(3);
		state.setFinished(true);
		state.setDateTimeStart(dateTime);
		state.setRound(round);
		state.setMaxScore(320);
		state.setLastScore(120);
		state.setUser(user);
		
		
		
		
		assertEquals(round, state.getRound());
		assertEquals(challenge, state.getChallenge());
		assertEquals(user, state.getUser());
		assertEquals(dateTime,state.getDateTimeStart());
		assertEquals(3,state.getCurrentAttempt());
		assertEquals(320,state.getMaxScore(),0);
		assertEquals(120,state.getLastScore(),0);
		assertTrue(state.isFinished());
		
		
	}
	

}
