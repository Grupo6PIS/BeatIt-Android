package com.g6pis.beatitapp.challenges.throwthephone;

import static org.junit.Assert.*;

import org.junit.Test;

public class ThrowThePhoneTest {

	@Test
	public void constructorTest() {
		ThrowThePhone throwThePhone = new ThrowThePhone("5", "Throw The Phone", 1, 3, "");
		
		assertEquals("5",throwThePhone.getChallengeId());
		assertEquals("Throw The Phone", throwThePhone.getName());
		assertEquals(1,throwThePhone.getLevel());
		assertEquals(3,throwThePhone.getMaxAttempt());
		assertEquals(100,throwThePhone.getMinHeight());
		
		throwThePhone = new ThrowThePhone("5", "Throw The Phone", 2, 3, "");
		assertEquals(200,throwThePhone.getMinHeight());
	}
	
	@Test
	public void calculateScoreTest(){
		ThrowThePhone throwThePhone = new ThrowThePhone("5", "Throw The Phone", 1, 3, "");
		
		throwThePhone.setHeightReached(500);
		
		assertEquals(500, throwThePhone.getHeightReached(),0);
		assertEquals(460,throwThePhone.calculateScore());
		
		
	}

}
