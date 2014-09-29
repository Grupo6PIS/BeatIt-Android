package com.g6pis.beatit.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChallengeTest {

	@Test
	public void ConstructorTest() {
		Challenge challenge = new Challenge("1","Challenge 1","Challenge 1 description", 7, 2, 3);
		
		assertEquals("1",challenge.getChallengeId());
		assertEquals("Challenge 1",challenge.getName());
		assertEquals("Challenge 1 description", challenge.getDescription());
		assertEquals(7,challenge.getDuration());
		assertEquals(2, challenge.getLevel());
		assertEquals(3,challenge.getMaxAttempt());
	}
	
	@Test
	public void SettersTest(){
		Challenge challenge = new Challenge("1","Challenge 1","Challenge 1 description", 7, 2, 3);
		
		
		challenge.setChallengeId("2");
		challenge.setDescription("Challenge 2 description");
		challenge.setName("Challenge 2");
		challenge.setDuration(3);
		challenge.setLevel(1);
		challenge.setMaxAttempt(4);
		
		assertEquals("2",challenge.getChallengeId());
		assertEquals("Challenge 2",challenge.getName());
		assertEquals("Challenge 2 description", challenge.getDescription());
		assertEquals(3,challenge.getDuration());
		assertEquals(1, challenge.getLevel());
		assertEquals(4,challenge.getMaxAttempt());
	}

}
