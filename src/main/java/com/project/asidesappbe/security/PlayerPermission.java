package com.project.asidesappbe.security;

public enum PlayerPermission {
    MATCH_ADMIN("match:admin"),
    GROUP_ADMIN("group:admin"),
    PLAYER_UPDATE("player:admin");

    private final String permission;

    PlayerPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
