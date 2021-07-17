package com.project.asidesappbe.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.asidesappbe.security.PlayerPermission.*;

public enum PlayerRole {
    GROUPPLAYER(Sets.newHashSet(
            PLAYER_UPDATE)
    ),
    GROUPADMIN(Sets.newHashSet(
        MATCH_ADMIN,
        GROUP_ADMIN,
        PLAYER_UPDATE)
    );

    private final Set<PlayerPermission> permissions;

    PlayerRole(Set<PlayerPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<PlayerPermission> getPermissions() {
        return new HashSet<>(permissions);
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissionsAsGrantedAuthority = collectPermissions();

        permissionsAsGrantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissionsAsGrantedAuthority;
    }

    private Set<SimpleGrantedAuthority> collectPermissions() {
        return getPermissions().
                stream().
                map(playerPermission -> new SimpleGrantedAuthority(playerPermission.getPermission()))
                .collect(Collectors.toSet());
    }
}
