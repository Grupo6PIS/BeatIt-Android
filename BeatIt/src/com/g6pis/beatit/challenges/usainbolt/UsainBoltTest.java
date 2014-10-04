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
		assertEquals(speeds,usain.getSpeeds());
	}
	
	@Test
	public void SpeedsTest(){
		UsainBolt usain = new UsainBolt("1", "Usain Bolt", 2, 3);
		
		Set<Double> speeds = new HashSet<Double>();
		Double maxSpeed = 0d;
		
		for(int i = 20; i < 35; i++){
			usain.addSpeed((double)i);
			speeds.add((double) i);
			if(i > maxSpeed){
				maxSpeed = (double) i;
			}
		}
		
		Double avgSpeed = 0d;
		for (Double speed : speeds) {
			avgSpeed += speed;
		}
		avgSpeed /= speeds.size();
		
		double score = (maxSpeed + avgSpeed)*2;
		
		assertEquals(maxSpeed,usain.getMaxSpeed(), 0.0);
		assertEquals(score,usain.calculateScore(),0.0);
		assertEquals(avgSpeed,usain.getAvgSpeed(),0.0);
		
		
	}
	
	@Test
	public void settersTest(){
		UsainBolt usain = new UsainBolt("1", "Usain Bolt", 2, 3);
		
		Set<Double> speeds = new HashSet<Double>();
		Double maxSpeed = 0d;
		
		for(int i = 20; i < 35; i++){
			speeds.add((double) i);
			if(i > maxSpeed){
				maxSpeed = (double) i;
			}
		}
		
		Double avgSpeed = 0d;
		for (Double speed : speeds) {
			avgSpeed += speed;
		}
		avgSpeed /= speeds.size();
		
		double score = (maxSpeed + avgSpeed)*2;
		
		usain.setAvgSpeed(avgSpeed);
		usain.setMaxSpeed(maxSpeed);
		usain.setSpeeds(speeds);
		
		assertEquals(maxSpeed,usain.getMaxSpeed(), 0.0);
		assertEquals(speeds,usain.getSpeeds());
		assertEquals(avgSpeed,usain.getAvgSpeed(),0.0);
		
	}

}
