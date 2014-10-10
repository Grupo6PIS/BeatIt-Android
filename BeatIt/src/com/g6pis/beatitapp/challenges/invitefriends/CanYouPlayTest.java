package com.g6pis.beatitapp.challenges.invitefriends;
/*package com.g6pis.beatit.challenges.invitefriends;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CanYouPlayTest {

	@Test
	public void constructorTest() {
		CanYouPlay canYouPlay = new CanYouPlay("3", "Can You Play?", 2, 3);
		
		List<String> phones = new ArrayList<String>();
		
		assertEquals("3",canYouPlay.getChallengeId());
		assertEquals(2,canYouPlay.getLevel());
		assertEquals(3,canYouPlay.getMaxAttempt());
		assertEquals("Can You Play?",canYouPlay.getName());
		assertEquals(phones,canYouPlay.getPhones());
		assertEquals(1,canYouPlay.getMinFBPost());
		assertEquals(4,canYouPlay.getMinSMS());
	}
	
	@Test
	public void scoreTest(){
		CanYouPlay canYouPlay = new CanYouPlay("3", "Can You Play?", 2, 3);
		
		List<String> phones = new ArrayList<String>();
		
		canYouPlay.addPhone("123456");
		phones.add("123456");
		canYouPlay.smsSent();
		canYouPlay.addPhone("789");
		phones.add("789");
		canYouPlay.smsSent();
		canYouPlay.addPhone("123789");
		phones.add("123789");
		canYouPlay.smsSent();
		canYouPlay.addPhone("123");
		phones.add("123");
		canYouPlay.smsSent();
		
		int score = (int) Math.round(canYouPlay.calculateScore());
		assertEquals(0,score);
		canYouPlay.fbPost();
		
		score = (int) Math.round(canYouPlay.calculateScore());
		assertEquals(130,score);

		assertEquals(phones,canYouPlay.getPhones());
	}

}
*/