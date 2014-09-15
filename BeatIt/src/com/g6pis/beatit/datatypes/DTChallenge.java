package com.g6pis.beatit.datatypes;

public class DTChallenge {
	
	private String name;
	private String description;
	private Integer duration;
	private Integer level;
	private DTDateTime dateTimeStart;

	
	public DTChallenge(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public DTDateTime getDateTimeStart() {
		return dateTimeStart;
	}
	public void setDateTimeStart(DTDateTime dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	

}
