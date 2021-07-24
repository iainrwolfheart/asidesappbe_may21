package com.project.asidesappbe.models;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.*;

@Document(collection = "asidesGroups")
public class Group {

	@Id
	@GeneratedValue
	private ObjectId _groupId;

	private String groupName;
	private String inviteCode;
	@OneToMany(mappedBy = "asidesPlayers")
	private Set<ObjectId> players;
	@OneToOne(mappedBy = "asidesMatches")
	private ObjectId _nextMatchId;

	public Group(){}

//	Create Group Constructor
	public Group(String groupName, ObjectId _playerId) {
		this.groupName = groupName;
		this.players = new HashSet<>();
		this.players.add(_playerId);
		this.inviteCode = generateInviteCode();
	}

	public ObjectId get_groupId() {
		return _groupId;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<ObjectId> getPlayers() {
		return players;
	}

	public void addPlayer(ObjectId _playerId) {
		this.players.add(_playerId);
	}

	public void removePlayer(ObjectId _playerId) {
		this.players.remove(_playerId);
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public ObjectId get_nextMatchId() {
		return _nextMatchId;
	}

	public void set_nextMatchId(ObjectId _nextMatchId) {
		this._nextMatchId = _nextMatchId;
	}

	private String generateInviteCode() {

		List<Character> charList = generateAlphanumericCharacterList();
		StringBuilder inviteCodeBuilder = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			inviteCodeBuilder.append(charList.get(new Random().nextInt(charList.size())));
		}

		return inviteCodeBuilder.toString();
	}

	private static List<Character> generateAlphanumericCharacterList() {

		ArrayList<Character> alphanumericCharacters = new ArrayList<>();

		for (char c = 48; c <= 122; c++) {
			if (Character.isDigit(c) || Character.isLetter(c)) {
				alphanumericCharacters.add(c);
			}
		}

		return alphanumericCharacters;
	}

@Override
	public String toString() {
		JSONObject groupObject = new JSONObject();
		JSONArray playerArr = new JSONArray(players.toString());

		groupObject.put("_groupId", _groupId.toString());
		groupObject.put("groupName", groupName);
		groupObject.put("players", playerArr);
		groupObject.put("inviteCode", inviteCode);
	try {
		groupObject.put("_nextMatchId", _nextMatchId);
	} catch (NullPointerException npe) {
		System.out.println("Group.toString method null pointer on _nextMatchId: " + npe.getMessage());
	}
		return groupObject.toString();
	}
}