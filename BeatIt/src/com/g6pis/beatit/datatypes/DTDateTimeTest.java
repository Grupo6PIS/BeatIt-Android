package com.g6pis.beatit.datatypes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DTDateTimeTest {

	@Test
	public void stringConstructorTest() {
		DTDateTime dateTime = new DTDateTime();
		
		String stringDateTime = dateTime.toString();
		
		DTDateTime dateTimeString = new DTDateTime(stringDateTime);
		
		assertEquals(dateTimeString.getDay(),dateTime.getDay());
		assertEquals(dateTimeString.getYear(),dateTime.getYear());
		assertEquals(dateTimeString.getMonth(),dateTime.getMonth());
		
		assertEquals(dateTimeString.getMinute(),dateTime.getMinute());
		assertEquals(dateTimeString.getHour(),dateTime.getHour());
		assertEquals(dateTimeString.getSecond(),dateTime.getSecond());
		
	}

}
 