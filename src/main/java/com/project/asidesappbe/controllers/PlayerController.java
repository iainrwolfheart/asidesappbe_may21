package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.models.TestPlayerModel;
import com.project.asidesappbe.repositories.PlayerRepository;
import com.project.asidesappbe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.project.asidesappbe.constants.RouteConstants.GETALL_ENDPOINT;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/v1/players")
public class PlayerController {

    private static final List<TestPlayerModel> PLAYERS = Arrays.asList(
            new TestPlayerModel(1, "Iain", "iain@email.com", "iain"),
            new TestPlayerModel(2, "Richie", "Richie@email.com", "Richie"),
            new TestPlayerModel(3, "Will", "Will@email.com", "Will"),
            new TestPlayerModel(4, "BennyBenBenBen", "BennyBenBenBen@email.com", "BennyBenBenBen")
    );

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;
//    @Autowired
//    private JwtService jwtService;

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

    @GetMapping(path = "{id}")
    public TestPlayerModel getPlayerById(@PathVariable("id") Integer id) {
        return PLAYERS.stream()
                .filter(player -> id.equals(player.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player " + id + " doesn't exist!"));
    }

    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    @GetMapping(value = GETALL_ENDPOINT)
    List<TestPlayerModel> getTestPlayers() {
        return PLAYERS;
    }
}
