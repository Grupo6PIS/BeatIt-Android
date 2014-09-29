package com.g6pis.beatit.entities;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.g6pis.beatit.datatypes.DTDateTime;

public class RoundTest {

	@Test
	public void constructorTest() {
		Map<String,Challenge> challenges = new HashMap<String, Challenge>();
		
		for(int i=0; i<10; i++){
			Challenge challenge = new Challenge(Integer.toString(i),"Challenge "+i,"Challenge "+i+" description", 7, 2, 3);
			challenges.put(Integer.toString(i), challenge);
		}
		
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		
		Round round = new Round("Round 1",dateTimeStart, dateTimeFinish,challenges);
		
		assertEquals("Round 1", round.getRoundId());
		assertEquals(dateTimeStart,round.getDateTimeStart());
		assertEquals(dateTimeFinish, round.getDateTimeFinish());
		assertEquals(challenges,round.getChallenges());
			
		
	}
	
	@Test
	public void settersTest(){
		Map<String,Challenge> challenges = new HashMap<String, Challenge>();
		
		for(int i=0; i<10; i++){
			Challenge challenge = new Challenge(Integer.toString(i),"Challenge "+i,"Challenge "+i+" description", 7, 2, 3);
			challenges.put(Integer.toString(i), challenge);
		}
		
		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();
		dateTimeStart.setYear(2);
		dateTimeStart.setMonth(2);
		dateTimeStart.setDay(2);
		dateTimeStart.setHour(2);
		dateTimeStart.setMinute(2);
		dateTimeStart.setSecond(2);
		
		dateTimeFinish.setYear(3);
		dateTimeFinish.setMonth(3);
		dateTimeFinish.setDay(3);
		dateTimeFinish.setHour(3);
		dateTimeFinish.setMinute(3);
		dateTimeFinish.setSecond(3);
		
		Round round = new Round("Round 1",dateTimeStart, dateTimeFinish,challenges);
		
		challenges = new HashMap<String, Challenge>();
		
		for(int i=10; i<20; i++){
			Challenge challenge = new Challenge(Integer.toString(i),"Challenge "+i,"Challenge "+i+" description", 7, 2, 3);
			challenges.put(Integer.toString(i), challenge);
		}
		

		
		
		dateTimeStart.setYear(dateTimeStart.getYear() + 2);
		dateTimeStart.setMonth(dateTimeStart.getMonth() + 2);
		dateTimeStart.setDay(dateTimeStart.getDay() + 2);
		dateTimeStart.setHour(dateTimeStart.getHour() + 2);
		dateTimeStart.setMinute(dateTimeStart.getMinute() + 2);
		dateTimeStart.setSecond(dateTimeStart.getSecond() + 2);
		
		dateTimeFinish.setYear(dateTimeFinish.getYear() + 3);
		dateTimeFinish.setMonth(dateTimeFinish.getMonth() + 3);
		dateTimeFinish.setDay(dateTimeFinish.getDay() + 3);
		dateTimeFinish.setHour(dateTimeFinish.getHour() + 3);
		dateTimeFinish.setMinute(dateTimeFinish.getMinute() + 3);
		dateTimeFinish.setSecond(dateTimeFinish.getSecond() + 3);
		
		
		round.setRoundId("Round 2");
		round.setDateTimeStart(dateTimeStart);
		round.setDateTimeFinish(dateTimeFinish);
		round.setChallenges(challenges);
		
		assertEquals("Round 2", round.getRoundId());
		assertEquals(dateTimeStart,round.getDateTimeStart());
		assertEquals(dateTimeFinish, round.getDateTimeFinish());
		assertEquals(challenges,round.getChallenges());
		
	}

}
