package com.g6pis.beatit.entities;

public class User {
	private String userId;
	private String fbId;
	private String firstName;
	private String lastName;
	private String country;
	private String imageURL;
	
	public User(String userId, String fbId, String firstName, String lastName,
			String country, String imageURL) {
		this.userId = userId;
		this.fbId = fbId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.imageURL = imageURL;
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

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	
	

}
