package com.project.asidesappbe.models;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Document(collection = "asidesPlayers")
public class Player {
		
	@Id
	@GeneratedValue
	private ObjectId _playerId;
	@ManyToOne
	@JoinColumn(name="_groupId")
	private ObjectId _groupId;
	private String username;
	private String email;
	private String password;
	private int rating;

	public Player(){}
	
	//LITE
	public Player(String username) {
		this.username = username;
	}
	
	//SIGNUP
	public Player(String username, String email, String password) {
		this(email, password);
		this.username = username;
		this.rating = 50; // THIS IS NOT WORKING...
	}
	
	//LOGIN
	public Player(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public ObjectId get_playerId() {
		return _playerId;
	}

	public void set_playerId(ObjectId _playerId) {
		this._playerId = _playerId;
	}

	public ObjectId get_groupId() {
		return _groupId;
	}

	public void set_groupId(ObjectId _groupId) {
		this._groupId = _groupId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString () {
		JSONObject playerObject = new JSONObject();
		playerObject.put("_playerId", _playerId.toString());
		playerObject.put("username", username);
		playerObject.put("email", email);
		playerObject.put("rating", rating);

		try {
			playerObject.put("_groupId", _groupId.toString());
		} catch (NullPointerException npe) {
			System.out.println("Setting _groupId to Player upon sign up. "
					+ npe.getMessage());
		}
		return playerObject.toString();
	}
}
