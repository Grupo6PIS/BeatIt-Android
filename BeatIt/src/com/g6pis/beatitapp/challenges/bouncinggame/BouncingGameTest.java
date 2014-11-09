package com.g6pis.beatitapp.challenges.bouncinggame;

import static org.junit.Assert.*;

import org.junit.Test;

public class BouncingGameTest {

	@Test
	public void ConstructorTest() {
		BouncingGame bouncing = new BouncingGame("5", "Wake Me Up", 2, 3, "#E51400");
		
		assertEquals("5",bouncing.getChallengeId());
		assertEquals("Bouncing Game",bouncing.getName());
		assertEquals(2,bouncing.getLevel());
		assertEquals(3,bouncing.getMaxAttempt());
	}
	
	@Test
	public void settersTest(){
		BouncingGame bouncing = new BouncingGame("2", "Bouncing Game", 2, 3, "#E51400");

		
	}
	
}
