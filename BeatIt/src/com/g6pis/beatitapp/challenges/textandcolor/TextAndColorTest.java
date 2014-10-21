package com.g6pis.beatitapp.challenges.textandcolor;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextAndColorTest {

	@Test
	public void constructorTest() {
		TextAndColor textAndColor = new TextAndColor("8", "Text & Color", 2, 3, "#0050EF");
		
		assertEquals("8",textAndColor.getChallengeId());
		assertEquals("Text & Color",textAndColor.getName());
		assertEquals(2,textAndColor.getLevel());
		assertEquals(3,textAndColor.getMaxAttempt());
		assertEquals("#0050EF",textAndColor.getColor());
		assertEquals(30,textAndColor.getCount());
		assertEquals(1500,textAndColor.getTime());
		assertEquals(15,textAndColor.getMinCount());
		assertEquals(0,textAndColor.getCurrentCount());
		
		
		textAndColor = new TextAndColor("8", "Text & Color", 1, 3, "#0050EF");
		
		assertEquals("8",textAndColor.getChallengeId());
		assertEquals("Text & Color",textAndColor.getName());
		assertEquals(1,textAndColor.getLevel());
		assertEquals(3,textAndColor.getMaxAttempt());
		assertEquals("#0050EF",textAndColor.getColor());
		assertEquals(15,textAndColor.getCount());
		assertEquals(1700,textAndColor.getTime());
		assertEquals(6,textAndColor.getMinCount());
		assertEquals(0,textAndColor.getCurrentCount());
		
	}
	
	@Test
	public void scoreTest() {
		TextAndColor textAndColor = new TextAndColor("8", "Text & Color", 1, 3, "#0050EF");
		
		
		assertEquals(0,textAndColor.getCurrentCount());
		
		textAndColor.successful();
		textAndColor.successful();
		
		assertEquals(2,textAndColor.getCurrentCount());
		assertEquals(0,textAndColor.calculateScore());
		assertFalse(textAndColor.isCompleted());
		
		textAndColor.reset();
		
		for(int i=0;i<14;i++){
			textAndColor.successful();
		}
		textAndColor.unsuccessful();
		
		assertEquals(15,textAndColor.getCurrentCount());
		assertEquals(64,textAndColor.calculateScore());
		assertTrue(textAndColor.isCompleted());
		
	}

}
