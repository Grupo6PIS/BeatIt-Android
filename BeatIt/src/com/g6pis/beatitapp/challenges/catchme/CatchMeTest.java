package com.g6pis.beatitapp.challenges.catchme;
/*
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.g6pis.beatitapp.challenges.textandcolor.TextAndColor;

public class CatchMeTest {
	
	
	@Test
	public void constructorTest() {
		CatchMe catchMe = new CatchMe("7", "Catch Me", 2, 3, "#D80073");
		
		assertEquals("7",catchMe.getChallengeId());
		assertEquals("Catch Me",catchMe.getName());
		assertEquals(2,catchMe.getLevel());
		assertEquals(3,catchMe.getMaxAttempt());
		assertEquals("#D80073",catchMe.getColor());
		assertEquals(30000,catchMe.getTime());
		assertEquals(500,catchMe.getTimeSpan());
		assertEquals(0,catchMe.getCurrentCount());
		
		
		catchMe = new CatchMe("7", "Catch Me", 1, 3, "#D80073");
		
		assertEquals("7",catchMe.getChallengeId());
		assertEquals("Catch Me",catchMe.getName());
		assertEquals(1,catchMe.getLevel());
		assertEquals(3,catchMe.getMaxAttempt());
		assertEquals("#D80073",catchMe.getColor());
		assertEquals(45000,catchMe.getTime());
		assertEquals(500,catchMe.getTimeSpan());
		assertEquals(0,catchMe.getCurrentCount());
		
		
	}
	
	@Test
	public void scoreTest() {
		CatchMe catchMe = new CatchMe("7", "Catch Me", 2, 3, "#D80073");
		
		
		assertEquals(0,catchMe.getCurrentCount());
		
		catchMe.successful();
		catchMe.successful();
		
		assertEquals(2,catchMe.getCurrentCount());
		assertEquals(2,catchMe.calculateScore(2));
		assertFalse(catchMe.isCompleted());

		
		catchMe.reset();
		
		for(int i=0;i<15;i++){
			catchMe.successful();
		}
		catchMe.unsuccessful();
		
		assertEquals(15,catchMe.getCurrentCount());
		assertEquals(75,catchMe.calculateScore(15));
		assertTrue(catchMe.isCompleted());
		
	}

}
*/