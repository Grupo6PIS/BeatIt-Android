package com.g6pis.beatitapp.challenges.bouncinggame;
/*
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
		
		int collision_times = 17;
		float decrease_radius_rate = 125;
		long time = 30;
		
		bouncing.setCollision_times(collision_times);
		bouncing.setDecrease_radius_rate(decrease_radius_rate);
		bouncing.setTime(time);

		assertEquals(collision_times,bouncing.getCollision_times());
		assertEquals((long) decrease_radius_rate, (long) bouncing.getDecrease_radius_rate());
		assertEquals(time,bouncing.getTime());
		assertEquals((long) (collision_times*5), (long) bouncing.calculateScore());
		
		bouncing.reset();
		assertEquals(0,bouncing.getCollision_times());
	}
	
}
*/