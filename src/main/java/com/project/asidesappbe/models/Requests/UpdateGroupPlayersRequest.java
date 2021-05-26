package com.project.asidesappbe.models.Requests;

public class UpdateGroupPlayersRequest {

    private String _groupId;
    private String _playerId;

    public UpdateGroupPlayersRequest(String _groupId, String _playerId) {
        this._groupId = _groupId;
        this._playerId = _playerId;
    }

    public String get_groupId() {
        return _groupId;
    }

    public String get_playerId() {
        return _playerId;
    }
}
