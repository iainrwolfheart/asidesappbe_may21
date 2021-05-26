package com.project.asidesappbe.services;

import com.project.asidesappbe.models.Group;
import com.project.asidesappbe.models.Match;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.models.Requests.CreateMatchRequest;
import com.project.asidesappbe.models.Requests.UpdateMatchPlayersRequest;
import com.project.asidesappbe.repositories.GroupRepository;
import com.project.asidesappbe.repositories.MatchRepository;
import com.project.asidesappbe.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    PlayerRepository playerRepository;

    public ResponseEntity<String> createMatch(@Valid CreateMatchRequest matchToCreate) {
        Group groupToAddMatch = groupRepository.findBy_groupId(matchToCreate.get_groupId());
        Player playerToAddToMatch = playerRepository.findBy_playerId(matchToCreate.get_playerId());

        Match savedMatch = saveNewMatch(
            new Match(
                groupToAddMatch.get_groupId(),
                matchToCreate.getMatchDate(),
                matchToCreate.getLocation(),
                playerToAddToMatch.get_playerId()
            )
        );

        addMatchIdToGroup(savedMatch, groupToAddMatch);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMatch.toString());
    }

    private Match saveNewMatch(Match matchToSave) {
        return matchRepository.save(matchToSave);
    }

//    Should this return something??
    private void addMatchIdToGroup(Match savedMatch, Group group) {
        group.set_nextMatchId(savedMatch.get_matchId());
        groupRepository.save(group);
    }

    public ResponseEntity<String> addOrRemovePlayerInMatchObject(@Valid UpdateMatchPlayersRequest request) {
//      Handle fringe case of _matchId not found -> NullPointerException

        Match matchToUpdateListOfAvailablePlayers = matchRepository.findBy_matchId(request.get_matchId());
        Player playertoUpdate = playerRepository.findBy_playerId(request.get_playerId());

        matchToUpdateListOfAvailablePlayers.addOrRemovePlayer(playertoUpdate.get_playerId());

        matchRepository.save(matchToUpdateListOfAvailablePlayers);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchToUpdateListOfAvailablePlayers.toString());
    }
//  Make one call... Custom repo query??
    public ResponseEntity<String> cancelMatch(String _matchId) {
        Match matchToDelete = matchRepository.findBy_matchId(_matchId);

        matchRepository.deleteById(matchToDelete.get_matchId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Match deleted.");
    }
}
