package com.g6pis.beatit.datatypes;

public class DTRanking {
	private String userName;
	private Integer score;
	private Integer position;
	private String imageURL;
	
	public DTRanking(String userName, Integer puntaje, Integer position,
			String imagen) {
		super();
		this.userName = userName;
		this.score = puntaje;
		this.position = position;
		this.imageURL = imagen;
	}
	
	public String getUserName() {
		return userName;
	}

	public Integer getScore() {
		return score;
	}

	public String getImageURL() {
		return imageURL;
	}
	
	public Integer getPosition() {
		return position;
	}
	
}
