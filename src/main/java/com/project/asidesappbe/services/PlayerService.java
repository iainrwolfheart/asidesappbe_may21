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

import static com.project.asidesappbe.security.PlayerRole.GROUPPLAYER;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Player foundPlayerDetails;

//    TO DO
//    Update authorities of player when they create a new group
//    Update authorities of player when they DELETE a group

    /*
    Redundant method now w/Spring Security??
     */
    public ResponseEntity<String> loginPlayer(Player playerToLogin) {
        //		ADD OPTION FOR USER TO ENTER USERNAME INSTEAD OF EMAIL! - MODEL
        Optional<Player> foundPlayerDetails = loadUserByEmail(playerToLogin.getEmail());

        if (!foundPlayerDetails.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Unsuccessful login: Email not found.");
        }
        else {
            Boolean isPasswordCorrect = passwordEncoder.matches(playerToLogin.getPassword(), foundPlayerDetails.get().getPassword());

            if (!isPasswordCorrect) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unsuccessful login: Incorrect password");
            }
            else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(foundPlayerDetails.toString());
            }
        }
    }

    /*
    Currently sets all registering users to GROUP_ADMIN privileges
     */
    public ResponseEntity<String> registerPlayer(Player playerToSignUp) {
        if (userExists(playerToSignUp.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists.");
        }

        playerToSignUp.set_playerId(ObjectId.get());
        playerToSignUp.setPassword(passwordEncoder.encode(playerToSignUp.getPassword()));
        playerToSignUp.setAuthorities(GROUPPLAYER.getGrantedAuthorities());
        playerRepository.save(playerToSignUp);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playerToSignUp.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<Player> foundPlayer = playerRepository.findByUsername(username);

        if (foundPlayer.isPresent()) return foundPlayer.get();

        else throw new UsernameNotFoundException("Username not found by loadUserByUsername method.");
    }

    public Optional<Player> loadUserByEmail(String email) throws UsernameNotFoundException {

        final Optional<Player> foundPlayer = playerRepository.findByEmail(email);

        if (foundPlayer.isPresent()) return foundPlayer;

        else throw new UsernameNotFoundException("Email not found by loadUserByEmail method.");
    }

    /*
    Currently only checks username uniqueness, NOT email...
     */
    public boolean userExists(String username) {
        return playerRepository.findByUsername(username).isPresent();
    }

    protected ObjectId addGroupIdToPlayer(String playerId, ObjectId groupId) {
        Player player = playerRepository.findBy_playerId(playerId);
        player.set_groupId(groupId);
        playerRepository.save(player);
        return player.get_playerId();
    }

    protected ObjectId removeGroupIdFromPlayer(String playerId) {
        Player playerToRemoveGroupId = playerRepository.findBy_playerId(playerId);
        playerToRemoveGroupId.set_groupId(null);
        playerRepository.save(playerToRemoveGroupId);
        return playerToRemoveGroupId.get_playerId();
    }
}
