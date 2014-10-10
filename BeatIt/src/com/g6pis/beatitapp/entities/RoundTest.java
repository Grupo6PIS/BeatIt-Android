package com.g6pis.beatitapp.entities;
/*package com.g6pis.beatit.entities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.g6pis.beatit.datatypes.DTDateTime;

public class RoundTest {

	@Test
	public void constructorTest() {
		List<Challenge> challenges = new ArrayList<Challenge>();

		for (int i = 0; i < 10; i++) {
			Challenge challenge = new Challenge(Integer.toString(i),
					"Challenge " + i, 2, 3);
			challenges.add(challenge);
		}

		DTDateTime dateTimeStart = new DTDateTime();
		DTDateTime dateTimeFinish = new DTDateTime();

		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish,
				challenges);

		assertEquals("Round 1", round.getRoundId());
		assertEquals(dateTimeStart, round.getDateTimeStart());
		assertEquals(dateTimeFinish, round.getDateTimeFinish());
		assertEquals(challenges, round.getChallenges());

	}

	@Test
	public void settersTest() {
		List<Challenge> challenges = new ArrayList<Challenge>();

		for (int i = 0; i < 10; i++) {
			Challenge challenge = new Challenge(Integer.toString(i),
					"Challenge " + i, 2, 3);
			challenges.add(challenge);
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

		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish,
				challenges);

		challenges = new ArrayList<Challenge>();

		for (int i = 10; i < 20; i++) {
			Challenge challenge = new Challenge(Integer.toString(i),
					"Challenge " + i, 2, 3);
			challenges.add(challenge);
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
		assertEquals(dateTimeStart, round.getDateTimeStart());
		assertEquals(dateTimeFinish, round.getDateTimeFinish());
		assertEquals(challenges, round.getChallenges());

		
	}

	@Test
	public void getChallengeTest() {
		List<Challenge> challenges = new ArrayList<Challenge>();

		for (int i = 0; i < 16; i++) {
			Challenge challenge = new Challenge(Integer.toString(i),
					"Challenge " + i, 2, 3);
			challenges.add(challenge);
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

		Round round = new Round("Round 1", dateTimeStart, dateTimeFinish,
				challenges);
		
		Challenge challenge = round.getChallenge("15");

		assertEquals("15", challenge.getChallengeId());
		assertEquals(2, challenge.getLevel());
		assertEquals(3, challenge.getMaxAttempt());
		assertEquals("Challenge 15", challenge.getName());
	}

}
*/