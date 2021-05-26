package com.project.asidesappbe.services;

import com.google.gson.Gson;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.models.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamsGeneratorService {
	
	Random selection = new Random();
	
	public JSONObject generateTeams() { //return JSONObject
		
		List<Player> availablePlayers = getAvailablePlayers();
		
		List<Team> unsortedTeams = assignAvailablePlayers(availablePlayers);
		
		List<Team> randomisedTeams = randomiseTeamsOneAndTwo(unsortedTeams);
		
		JSONObject teamsToSend = stringifyTeams(randomisedTeams);
		
		return teamsToSend;
	}
	
	public JSONObject generateLiteTeams(JSONArray availablePlayersObj) {

		List<Player> availablePlayers = getAvailablePlayers(availablePlayersObj);
		List<Team> unsortedTeams = assignAvailablePlayers(availablePlayers);
		List<Team> randomisedTeams = randomiseTeamsOneAndTwo(unsortedTeams);
		
		JSONObject teamsToSend = stringifyTeams(randomisedTeams);
		
		return teamsToSend;
	}

	private List<Player> getAvailablePlayers() {

		// Add call to DB to fetch members of group where canPlay == true
		// May eventually want to return a message if not enough players to create teams
		
		List<Player> availablePlayers = new ArrayList<>();
		availablePlayers.add(new Player("Iain"));
		availablePlayers.add(new Player("Mike"));
		availablePlayers.add(new Player("Rich"));
		availablePlayers.add(new Player("Matt"));
		availablePlayers.add(new Player("Tom"));
		availablePlayers.add(new Player("Mark"));
		availablePlayers.add(new Player("Carl"));
		availablePlayers.add(new Player("Adam"));
		availablePlayers.add(new Player("Olleh"));
		
		return availablePlayers;
	}

//	Used to process LITE Group
	private List<Player> getAvailablePlayers(JSONArray availablePlayers) {

		ArrayList<Player> availablePlayersList = new ArrayList<>();
		for (int i = 0; i < availablePlayers.length(); i++) {
			availablePlayersList.add(new Player (availablePlayers.getString(i)));
		}
		return availablePlayersList;
	}

	private List<Team> assignAvailablePlayers(List<Player> availablePlayers) {
		
		List<Player> blueTeamPlayers = new ArrayList<>();
		List<Player> redTeamPlayers = new ArrayList<>();
		List<Team> teams = new ArrayList<>();
				
		for ( int count = availablePlayers.size() / 2; count > 0; count-- ) {
			int playerSelection = selection.nextInt(availablePlayers.size());
			blueTeamPlayers.add(availablePlayers.get(playerSelection));
			availablePlayers.remove(playerSelection);	
		}
		
		for ( int count = availablePlayers.size(); count > 0; count-- ) {
			
			int playerSelection = selection.nextInt(availablePlayers.size());
			redTeamPlayers.add(availablePlayers.get(playerSelection));
			availablePlayers.remove(playerSelection);
		}
		
		Team blueTeam = new Team(blueTeamPlayers);
		Team redTeam = new Team(redTeamPlayers);
		
		teams.add(blueTeam);
		teams.add(redTeam);
		
		return teams;
	}
	
	//IMPLEMENT RATINGS CHECK METHOD HERE
	
	private List<Team> randomiseTeamsOneAndTwo (List<Team> teams) {
		List<Team> randomisedTeams = new ArrayList<>();
		int teamSelection = selection.nextInt(teams.size());
		randomisedTeams.add(teams.get(teamSelection));
		teams.remove(teamSelection);
		randomisedTeams.add(teams.get(0));
		
		return randomisedTeams;
	}
	
	private JSONObject stringifyTeams(List<Team> randomisedTeams) {
		Gson gson = new Gson();
		JSONObject finalTeams = new JSONObject();
		try {
			finalTeams.put("redTeam", randomisedTeams.get(0).getPlayers());
			finalTeams.put("blueTeam", randomisedTeams.get(1).getPlayers());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return finalTeams;
	}

}
