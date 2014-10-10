package com.g6pis.beatitapp.entities;
/*package com.g6pis.beatit.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.g6pis.beatit.datatypes.DTDateTime;

public class StateTest {

	@Test
	public void ConstructorTest() {
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		List<Challenge> challenges = new ArrayList<Challenge>();
		
		Challenge challenge = new Challenge("1", "Challenge 1", 2, 3);
		challenges.add(challenge);
		
		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish, challenges);
		
		User user = new User("123", "456", "Johnnie", "Walker", "USA", "facebook.com");
		
		State state = new State(round, challenge, user);
		
		assertEquals(round, state.getRound());
		assertEquals(challenge, state.getChallenge());
		assertEquals(user, state.getUser());
		assertEquals(0,state.getCurrentAttempt());
		assertEquals(0,state.getMaxScore(),0);
		assertEquals(0,state.getLastScore(),0);
		assertFalse(state.isFinished());
	}
	
	@Test
	public void settersTest(){
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		List<Challenge> challenges = new ArrayList<Challenge>();
		
		Challenge challenge = new Challenge("1", "Challenge 1", 2, 3);
		challenges.add(challenge);
		
		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish, challenges);
		
		User user = new User("123", "456", "Johnnie", "Walker", "USA", "facebook.com");
		
		State state = new State(round, challenge, user);
		
		challenges = new ArrayList<Challenge>();
		
		challenge = new Challenge("2", "Challenge 2", 2, 3);
		challenges.add(challenge);
		
		round = new Round("Round 2", dateTimeStart, dateTimeFinish, challenges);
		
		user = new User("789", "987", "William", "Lawson", "England", "facebook.com");
		
		
		DTDateTime dateTime = new DTDateTime(6, 3, 1992, 9, 30, 0);
		
		state.setChallenge(challenge);
		state.setCurrentAttempt(2);
		state.setRound(round);
		state.setMaxScore(320);
		state.setNewScore(420);
		state.setUser(user);
		
		
		
		
		
		assertEquals(round, state.getRound());
		assertEquals(challenge, state.getChallenge());
		assertEquals(user, state.getUser());
		assertEquals(3,state.getCurrentAttempt());
		assertEquals(420,state.getMaxScore(),0);
		assertEquals(420,state.getLastScore(),0);
		assertTrue(state.isFinished());
		
		state.setFinished(false);
		state.setLastScore(125);
		state.setLastFinishDateTime(dateTime);
		assertFalse(state.isFinished());
		assertEquals(125,state.getLastScore(),0);
		assertEquals(dateTime,state.getLastFinishDateTime());
		
		
	}
	

}
*/