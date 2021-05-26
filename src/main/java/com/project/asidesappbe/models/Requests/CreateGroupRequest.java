package com.project.asidesappbe.models.Requests;

public class CreateGroupRequest {

    private String groupName;
    private String _playerId;

    public CreateGroupRequest(String groupName, String _playerId) {
        this.groupName = groupName;
        this._playerId = _playerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String get_playerId() {
        return _playerId;
    }
}
