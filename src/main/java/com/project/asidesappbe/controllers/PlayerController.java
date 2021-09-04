package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.repositories.PlayerRepository;
import com.project.asidesappbe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/v1/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;

    Player foundPlayerDetails;

    /**
     * @param player
     * @return responseEntity sent back to calling application. Includes CREATED status and useful information
     * about the player through player.toString() method.
     * Assuming a CREATED status is received, the client application should fire a further call to /login
     * in order to retrieve a JWT and access other endpoints
     */
    @PostMapping(value = RouteConstants.REGISTER_ENDPOINT)
    ResponseEntity<String> register(@Valid @RequestBody Player player) {
		return playerService.registerPlayer(player);
    }

    @RequestMapping(value = RouteConstants.LOGOUT_ENDPOINT, method = RequestMethod.POST)
    public ResponseEntity<String> logout(@Valid @RequestBody String jwt) {

//        Invalidate JWT?

        return ResponseEntity.status(HttpStatus.OK).body("Logout Successful");
    }}
