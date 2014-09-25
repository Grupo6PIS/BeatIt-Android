package com.g6pis.beatit.classes;

public class User {
	private String userId;
	private String fbId;
	private String firstName;
	private String lastName;
	private String country;
	//TODO profilePicture
	
	public User(String userId, String fbId, String firstName, String lastName,
			String country) {
		super();
		this.userId = userId;
		this.fbId = fbId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	

}
