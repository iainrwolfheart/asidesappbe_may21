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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtService jwtService;

    Player foundPlayerDetails;

    public ResponseEntity<String> loginPlayer(Player playerToLogin) {
        //		ADD OPTION FOR USER TO ENTER USERNAME INSTEAD OF EMAIL! - MODEL
        foundPlayerDetails = playerRepository.findByEmail(playerToLogin.getEmail());

        if (foundPlayerDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Unsuccessful login: Email not found.");
        }
        else {
            Boolean isPasswordCorrect = passwordService.isCorrectPassword(playerToLogin.getPassword(), foundPlayerDetails.getPassword());

            if (!isPasswordCorrect) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unsuccessful login: Incorrect password");
            }
            else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Token", jwtService.generateToken(playerToLogin))
                        .body(foundPlayerDetails.toString());
            }
        }
    }

    public ResponseEntity<String> registerPlayer(Player playerToSignUp) {
        if (userExists(playerToSignUp.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists.");
        }

        playerToSignUp.set_playerId(ObjectId.get());
        playerToSignUp.setPassword(passwordService.encryptUserPassword(playerToSignUp.getPassword()));

        playerRepository.save(playerToSignUp);

        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .header("token", jwtService.generateToken(playerToSignUp))
                .body(playerToSignUp.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<Player> foundPlayer = playerRepository.findByUsername(username);

        if (foundPlayer.isPresent()) return foundPlayer.get();

        else throw new UsernameNotFoundException("Username not found by loadUserByUsername method.");
    }

    /*
    Currently only checks username uniqueness, NOT email...
     */
    private boolean userExists(String username) {
        return playerRepository.findByUsername(username).isPresent();
    }
}
