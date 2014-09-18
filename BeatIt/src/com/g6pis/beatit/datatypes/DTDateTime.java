package com.g6pis.beatit.datatypes;

import java.text.SimpleDateFormat;

public class DTDateTime {
	
	private Integer day;
	private Integer month;
	private Integer year;
	private Integer hour;
	private Integer minute;
	private Integer second;
	
	public DTDateTime(){
		
	}
	
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public Integer getSecond() {
		return second;
	}
	public void setSecond(Integer second) {
		this.second = second;
	}

	@Override
	public String toString() {
		String date = "";
		
		if (day < 10)
			date = date + "0"+day;
		else
			date = date + day;
		
		if(month < 10)
			date = date + "/0"+month+"/"+year;
		else
			date = date + "/"+month+"/"+year;
		
		if(hour < 10)
			date = date + " 0"+hour;
		else
			date = date + " "+hour;
		
		if(minute < 10)
			date = date + ":0"+minute;
		else
			date = date + ":"+minute;
		
		if(second < 10)
			date = date + ":0"+second;
		else
			date = date + ":"+second;
		
		
		return date;
	}
	
	

}
