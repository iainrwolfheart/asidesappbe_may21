package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.repositories.PlayerRepository;
import com.project.asidesappbe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /*
    This endpoint likely redundant now.
    Spring Security default endpoint for hitting the appropriate filters is /login.
    As long as the returned JWT contains relevant data, this can be bye-byed
     */
    @PostMapping(value = RouteConstants.LOGIN_ENDPOINT)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> login(@Valid @RequestBody Player player) {
		return playerService.loginPlayer(player);
    }

    /*
    Client will need to fire both registration req and then, if reg successful, fire a login req in order
    to return a JWT and populate homepage
     */
    @PostMapping(value = RouteConstants.REGISTER_ENDPOINT)
    ResponseEntity<String> register(@Valid @RequestBody Player player) {
		return playerService.registerPlayer(player);
    }
}
