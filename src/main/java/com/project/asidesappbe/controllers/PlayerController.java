package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.models.TestPlayerModel;
import com.project.asidesappbe.repositories.PlayerRepository;
import com.project.asidesappbe.services.JwtService;
import com.project.asidesappbe.services.PasswordService;
import com.project.asidesappbe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtService jwtService;

    Player foundPlayerDetails;

    @PostMapping(value = RouteConstants.LOGIN_ENDPOINT)
    ResponseEntity<String> login(@Valid @RequestBody Player player) {
        return ResponseEntity.status(200).body("This works!");
//		return playerService.loginPlayer(player);
    }

    @PostMapping(value = RouteConstants.REGISTER_ENDPOINT)
    ResponseEntity<String> register(@Valid @RequestBody Player player) {
        return ResponseEntity.status(200).body("This works!");
//		return playerService.registerPlayer(player);
    }

    @GetMapping(path = "{id}")
    public TestPlayerModel getPlayerById(@PathVariable("id") Integer id) {
        return PLAYERS.stream()
                .filter(player -> id.equals(player.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player " + id + " doesn't exist!"));
    }
    @GetMapping(value = GETALL_ENDPOINT)
    List<TestPlayerModel> getTestPlayers() {
        return PLAYERS;
    }
}
