package com.g6pis.beatit.classes;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void constructorTest() {

		User user = new User("userId", "fbId", "firstName", "lastName",
				"country");

		assertEquals("userId", user.getUserId());
		assertEquals("fbId", user.getFbId());
		assertEquals("firstName", user.getFirstName());
		assertEquals("lastName", user.getLastName());
		assertEquals("country", user.getCountry());

	}

	@Test
	public void settersTest(){
		User user = new User("userId", "fbId", "firstName", "lastName",
				"country");
		
			user.setUserId("123456");
			user.setFbId("987654");
			user.setFirstName("Johnnie");
			user.setLastName("Walker");
			user.setCountry("Uruguay");
			
			assertEquals("123456",user.getUserId());
			assertEquals("987654",user.getFbId());
			assertEquals("Johnnie",user.getFirstName());
			assertEquals("Walker",user.getLastName());
			assertEquals("Uruguay",user.getCountry());
	}
}
