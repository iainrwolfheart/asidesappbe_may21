package com.project.asidesappbe.services;

import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.repositories.PlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.asidesappbe.security.PlayerRole.GROUPADMIN;
import static com.project.asidesappbe.security.PlayerRole.GROUPPLAYER;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Registers a new player
     * If exception is thrown, it indicates that either the username or email already exists in the database.
     * A getLocalizedMessage() is added to response body. This String can be parsed to ascertain the causing field
     * @param playerToSignUp includes username, email & password
     * @return responseEntity sent back to calling application via the Controller. Includes CREATED status and
     * useful information about the player through player.toString() method.
    */
    public ResponseEntity<String> registerPlayer(Player playerToSignUp) {
        playerToSignUp.set_playerId(ObjectId.get());
        playerToSignUp.setPassword(passwordEncoder.encode(playerToSignUp.getPassword()));
        playerToSignUp.setAuthorities(GROUPPLAYER.getGrantedAuthorities());

        try {
            playerRepository.save(playerToSignUp);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(playerToSignUp.toString());
        } catch (Exception mwe) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(mwe.getLocalizedMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<Player> foundPlayer = playerRepository.findByUsername(username);

        if (foundPlayer.isPresent()) return foundPlayer.get();

        else throw new UsernameNotFoundException("Username not found by loadUserByUsername method.");
    }

    private Player loadPlayerByIdString(String _playerId) {
        return playerRepository.findBy_playerId(_playerId);
    }

    public boolean usernameExists(String username) {
        return playerRepository.findByUsername(username).isPresent();
    }

    protected ObjectId addGroupIdToPlayer(String playerId, ObjectId groupId) {
        Player player = loadPlayerByIdString(playerId);
        player.set_groupId(groupId);
        playerRepository.save(player);
        return player.get_playerId();
    }

    protected ObjectId removeGroupIdFromPlayer(String playerId) {
        Player playerToRemoveGroupId = playerRepository.findBy_playerId(playerId);

        playerToRemoveGroupId.set_groupId(null);
        if (playerToRemoveGroupId.getAuthorities()
                .stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_GROUPADMIN"))
        ) {
            playerToRemoveGroupId.setAuthorities(GROUPPLAYER.getGrantedAuthorities());
        }
        playerRepository.save(playerToRemoveGroupId);
        return playerToRemoveGroupId.get_playerId();
    }

    protected Player givePlayerGroupAdminPrivileges(String playerId) {
        Player playerToUpdate = loadPlayerByIdString(playerId);
        playerToUpdate.setAuthorities(GROUPADMIN.getGrantedAuthorities());
        return playerRepository.save(playerToUpdate);
    }
}
