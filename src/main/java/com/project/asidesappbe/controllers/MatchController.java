package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Requests.CreateMatchRequest;
import com.project.asidesappbe.models.Requests.UpdateMatchPlayersRequest;
import com.project.asidesappbe.repositories.MatchRepository;
import com.project.asidesappbe.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchRepository matchRepository;

    @PostMapping(value = RouteConstants.CREATE_ENDPOINT)
    public ResponseEntity<String> createMatch(@Valid @RequestBody CreateMatchRequest matchToCreate) {
        return matchService.createMatch(matchToCreate);
    }

    @PutMapping(value = RouteConstants.UPDATEMATCHPLAYERS)
    ResponseEntity<String> addOrRemoveMatchPlayer(
            @Valid @RequestBody UpdateMatchPlayersRequest request) {
        return matchService.addOrRemovePlayerInMatchObject(request);
    }

    @GetMapping(value = RouteConstants.GETBYID_ENDPOINT + "/{_matchId}")
    ResponseEntity<String> getMatchById(@Valid @PathVariable String _matchId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchRepository.findBy_matchId(_matchId).toString());
    }

    @DeleteMapping(value = RouteConstants.DELETEBYID_ENDPOINT + "/{_matchId}")
    ResponseEntity<String> deleteMatch(@Valid @PathVariable String _matchId) {
        return matchService.cancelMatch(_matchId);
    }
}
