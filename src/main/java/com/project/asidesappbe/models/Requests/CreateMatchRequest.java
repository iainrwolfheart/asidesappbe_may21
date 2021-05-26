package com.project.asidesappbe.models.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateMatchRequest {

    private String _groupId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime matchDate;
    private String location;
    private String _playerId;

    public CreateMatchRequest(String _groupId, LocalDateTime matchDate, String location, String _playerId) {
        this._groupId = _groupId;
        this.matchDate = matchDate;
        this.location = location;
        this._playerId = _playerId;
    }

    public String get_groupId() {
        return _groupId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public String getLocation() {
        return location;
    }

    public String get_playerId() {
        return _playerId;
    }
}
