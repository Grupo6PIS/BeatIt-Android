package com.g6pis.beatitapp.datatypes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DTDateTime {

	private Integer day;
	private Integer month;
	private Integer year;
	private Integer hour;
	private Integer minute;
	private Integer second;

	public DTDateTime() {
		Calendar calendar = new GregorianCalendar();

		second = calendar.get(Calendar.SECOND);
		minute = calendar.get(Calendar.MINUTE);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);

	}

	public DTDateTime(long utc) {
		Calendar calendar = new GregorianCalendar();
		Date date = new Date(utc);
		calendar.setTime(date);

		second = calendar.get(Calendar.SECOND);
		minute = calendar.get(Calendar.MINUTE);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
	}

	public DTDateTime(Integer day, Integer month, Integer year, Integer hour,
			Integer minute, Integer second) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public DTDateTime(String dateTime) {
			String[] spaceSplit = dateTime.split(" ");

			String date = spaceSplit[0];
			String time = spaceSplit[1];

			String[] dateSplit = date.split("/");
			day = Integer.parseInt(dateSplit[0]);
			month = Integer.parseInt(dateSplit[1]);
			year = Integer.parseInt(dateSplit[2]);

			String[] timeSplit = time.split(":");
			hour = Integer.parseInt(timeSplit[0]);
			minute = Integer.parseInt(timeSplit[1]);
			second = Integer.parseInt(timeSplit[2]);
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
			date = date + "0" + day;
		else
			date = date + day;

		if (month < 10)
			date = date + "/0" + month + "/" + year;
		else
			date = date + "/" + month + "/" + year;

		if (hour < 10)
			date = date + " 0" + hour;
		else
			date = date + " " + hour;

		if (minute < 10)
			date = date + ":0" + minute;
		else
			date = date + ":" + minute;

		if (second < 10)
			date = date + ":0" + second;
		else
			date = date + ":" + second;

		return date;
	}
	
	public String diff(DTDateTime dateTime){
		String diff;
		
		if(this.year - dateTime.getYear() == 0){
			if(this.month - dateTime.getMonth() == 0){
				if(this.day - dateTime.getDay() == 0){
					if(this.hour - dateTime.getHour() == 0){
						if(this.minute - dateTime.getMinute() == 0){
							if(this.second - dateTime.getSecond() == 0){
								diff = "equals";
							}else{
								diff = "second";
							}
						}else{
							diff = "minute";
						}
					}else{
						diff = "hour";
					}
				}else{
					diff = "day";
				}
			}else{
				diff = "month";
			}
		}else{
			diff = "year";
		}
		
		return diff;
	}
	
	public Integer diff(DTDateTime dateTime, String difference){
		if(difference.equals("year"))
			return Math.abs(this.year - dateTime.getYear());
		
		if(difference.equals("month"))
			return Math.abs(this.month - dateTime.getMonth());

		if(difference.equals("day"))
			return Math.abs(this.day - dateTime.getDay());

		if(difference.equals("hour"))
			return Math.abs(this.hour - dateTime.getHour());
		
		if(difference.equals("minute"))
			return Math.abs(this.minute - dateTime.getMinute());
		
		if(difference.equals("second"))
			return Math.abs(this.second - dateTime.getSecond());
		
		
		return 0;
		
		
	}

}
