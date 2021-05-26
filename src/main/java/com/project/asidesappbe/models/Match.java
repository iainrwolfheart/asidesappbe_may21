package com.project.asidesappbe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "asidesMatches")
public class Match {

    @Id
    @GeneratedValue
    private ObjectId _matchId;
    @OneToOne(mappedBy = "asidesGroups")
    private ObjectId _groupId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime matchDate;
    private String location; // May need type change...
    private List<ObjectId> _idsOfAvailablePlayers;
    private List<Team> teams = new ArrayList<>();

    public Match(){}

//    Create Match Constructor
    public Match(ObjectId _groupId, LocalDateTime matchDate, String location, ObjectId _playerId) {
        this._groupId = _groupId;
        this.matchDate = matchDate;
        this.location = location;
        this._idsOfAvailablePlayers = new ArrayList<>();
        this._idsOfAvailablePlayers.add(_playerId);
    }

    public ObjectId get_matchId() {
        return _matchId;
    }

    public ObjectId get_groupId() {
        return _groupId;
    }

    public void set_groupId(ObjectId _groupId) {
        this._groupId = _groupId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public String getLocation() {
        return location;
    }

    public List<ObjectId> getIdsOfAvailablePlayers() {
        return new ArrayList(_idsOfAvailablePlayers);
    }

    public List<ObjectId> addOrRemovePlayer(ObjectId _playerId) {
        if (this._idsOfAvailablePlayers.contains(_playerId)) {
            this._idsOfAvailablePlayers.remove(_playerId);
        } else {
            this._idsOfAvailablePlayers.add(_playerId);
        }
        return new ArrayList<>(this._idsOfAvailablePlayers);
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public String toString() {
        JSONObject matchObject = new JSONObject();
        JSONArray availablePlayersArr = new JSONArray(_idsOfAvailablePlayers.toString());

        matchObject.put("_matchId", _matchId.toString());
        matchObject.put("_groupId", _groupId.toString());
        matchObject.put("matchDate", matchDate);
        matchObject.put("location", location);
        try {
            matchObject.put("availablePlayers", availablePlayersArr);
            matchObject.put("teams", teams);
        } catch (NullPointerException npe) {
            System.out.println("Match.toString method null pointer on either availablePlayers or " +
                    "teams: " + npe.getMessage());
        }
        return matchObject.toString();
    }
}
