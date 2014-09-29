package com.g6pis.beatit.controllers;

//import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DataManagerTest {

	@Test
	public void DataManagerLoginTest() {
		DataManager dataManager = DataManager.getInstance();
		
		dataManager.login("123", "Johnnie", "Walker", "Scotland");
		
		assertEquals("123",dataManager.getUser().getFbId());
		assertEquals("Johnnie",dataManager.getUser().getFirstName());
		assertEquals("Walker",dataManager.getUser().getLastName());
		assertEquals("Scotland",dataManager.getUser().getCountry());
		
		dataManager.logout();
		
		assertEquals(null,dataManager.getUser());
		
		
		
	}

}
