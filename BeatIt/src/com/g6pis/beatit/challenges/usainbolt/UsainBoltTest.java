package com.g6pis.beatit.challenges.usainbolt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class UsainBoltTest {

	@Test
	public void ConstructorTest() {
		UsainBolt usain = new UsainBolt("1", "Usain Bolt", 2, 3);
		Set<Double> speeds = new HashSet<Double>();
		
		assertEquals("1",usain.getChallengeId());
		assertEquals("Usain Bolt",usain.getName());
		assertEquals(2,usain.getLevel());
		assertEquals(3,usain.getMaxAttempt());
		assertEquals(0,usain.getMaxSpeed(),0.0);
		assertEquals(0,usain.getAvgSpeed(),0.0);
	}
	
	@Test
	public void SpeedsTest(){
		UsainBolt usain = new UsainBolt("1", "Usain Bolt", 2, 3);
		
		Double speeds = 0d;
		Double maxSpeed = 0d;
		int speedCount = 0;
		
		for(int i = 20; i < 35; i++){
			usain.addSpeed((double)i);
			speeds += i;
			speedCount++;
			if(i > maxSpeed){
				maxSpeed = (double) i;
			}
		}
		
		Double avgSpeed = speeds/speedCount;
		
		double score = (maxSpeed + avgSpeed)*2;
		
		assertEquals(maxSpeed,usain.getMaxSpeed(), 0.0);
		assertEquals(score,usain.calculateScore(),0.0);
		assertEquals(avgSpeed,usain.getAvgSpeed(),0.0);
		
		
	}
	
	@Test
	public void settersTest(){
		UsainBolt usain = new UsainBolt("1", "Usain Bolt", 2, 3);
		
		Double speeds = 0d;
		Double maxSpeed = 0d;
		int speedCount = 0;
		for(int i = 20; i < 35; i++){
			speeds += i;
			speedCount++;
			if(i > maxSpeed){
				maxSpeed = (double) i;
			}
		}
		
		Double avgSpeed = speeds/speedCount;
		
		double score = (maxSpeed + avgSpeed)*2;
		
		usain.setAvgSpeed(avgSpeed);
		usain.setMaxSpeed(maxSpeed);
		
		assertEquals(maxSpeed,usain.getMaxSpeed(), 0.0);
		assertEquals(avgSpeed,usain.getAvgSpeed(),0.0);
		
	}

}

