package com.g6pis.beatitapp.challenges.textandcolor;

/*import static org.junit.Assert.*;

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
		assertEquals(1500,textAndColor.getTime());

		
		
		textAndColor = new TextAndColor("8", "Text & Color", 1, 3, "#0050EF");
		
		assertEquals("8",textAndColor.getChallengeId());
		assertEquals("Text & Color",textAndColor.getName());
		assertEquals(1,textAndColor.getLevel());
		assertEquals(3,textAndColor.getMaxAttempt());
		assertEquals("#0050EF",textAndColor.getColor());
		assertEquals(0,textAndColor.getCount());
		assertEquals(1700,textAndColor.getTime());
		
	}
	
	@Test
	public void scoreTest() {
		TextAndColor textAndColor = new TextAndColor("8", "Text & Color", 1, 3, "#0050EF");
		
		
		assertEquals(0,textAndColor.getCount());
		
		textAndColor.addCount();
		textAndColor.addCount();
		
		assertEquals(2,textAndColor.getCount());
		assertEquals(2,textAndColor.calculateScore());
		
		textAndColor.reset();
		int score = 0;
		for(int i=1;i<16;i++){
			textAndColor.addCount();
			if(i<=5){
				score += 1;
			}else if (i<=10){
				score += 5;
			}else
				score += 10;
		}
		
		assertEquals(15,textAndColor.getCount());
		assertEquals(score,textAndColor.calculateScore());
		
	}

}
*/