package com.project.asidesappbe.models.Requests;

public class UpdateMatchPlayersRequest {

    private String _matchId;
    private String _playerId;

    public UpdateMatchPlayersRequest(String _matchId, String _playerId) {
        this._matchId = _matchId;
        this._playerId = _playerId;
    }

    public String get_matchId() {
        return _matchId;
    }

    public String get_playerId() {
        return _playerId;
    }
}
