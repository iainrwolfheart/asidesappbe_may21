package com.project.asidesappbe.models;

import java.util.List;
//import java.util.Random;
//import org.json.simple.JSONArray;

public class Team {
	private List<Player> players;
//	private int teamRating;
//	private Player teamCaptain;

	public Team(List<Player> players) {
		this.players = players;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
//	public int getTeamRating() {
//		return teamRating;
//	}
	//needs testing
//	public void setTeamRating(List<Player> players) {
//		int totalPlayerRating = 0;
//		int maxRating = players.size() * 100;
//		
//		for (Player player : players) {
//		totalPlayerRating += player.getRating();
//		}
//		
//		this.teamRating = (totalPlayerRating / maxRating) * 100;
//	}
//
//	public Player getTeamCaptain() {
//		return teamCaptain;
//	}
//	//needs testing
//	public void setTeamCaptain(List<Player> players) {
//		Random random = new Random();
//		int captSelector = random.nextInt(players.size());
//		this.teamCaptain = players.get(captSelector);
//	}
	
}
