package com.g6pis.beatitapp.challenges.wakemeup;
/*
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class WakeMeUpTest {

	//private static final Integer NUMBER_OF_REPETITIONS_LEVEL1 = 3;
	private static final Integer NUMBER_OF_REPETITIONS_LEVEL2 = 4;
	//private static final long TIME_LEVEL1_3 = 3;
	private static final long TIME_LEVEL2_4 = 3;

	@Test
	public void ConstructorTest() {
		WakeMeUp wake = new WakeMeUp("2", "Wake Me Up", 2, 3, "#00ABA9");
		
		assertEquals("2",wake.getChallengeId());
		assertEquals("Wake Me Up",wake.getName());
		assertEquals(2,wake.getLevel());
		assertEquals(3,wake.getMaxAttempt());
		assertEquals(0,wake.getNumber_of_repetitions(),NUMBER_OF_REPETITIONS_LEVEL2);
		assertEquals(0,wake.getHidden_secs(),TIME_LEVEL2_4);
	}
	
	@Test
	public void settersTest(){
		WakeMeUp wake = new WakeMeUp("2", "Wake Me Up", 2, 3, "#00ABA9");
		
		int succeed_times = 3;
		long hidden_secs = 7;

		Integer number_of_repetitions = wake.getNumber_of_repetitions();
		long l_number_of_repetitions = number_of_repetitions.longValue();
		
		Integer expected_number_of_repetitions = 4;
		long l_expected_number_of_repetitions = expected_number_of_repetitions.longValue();
		
		
		wake.setSucceed_times(succeed_times);
		wake.setNumber_of_repetitions(expected_number_of_repetitions);
		wake.setHidden_secs(hidden_secs);
		
		assertEquals(succeed_times, wake.getSucceed_times());
		assertEquals(l_expected_number_of_repetitions, l_number_of_repetitions);
		assertEquals(hidden_secs, wake.getHidden_secs());
		assertEquals((long) (succeed_times*80), (long) wake.calculateScore());
		
		wake.reset();
		assertEquals(0,wake.getSucceed_times());
	}

}
*/