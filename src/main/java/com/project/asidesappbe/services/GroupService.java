package com.project.asidesappbe.services;

import com.project.asidesappbe.models.Group;
import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.models.Requests.CreateGroupRequest;
import com.project.asidesappbe.models.Requests.UpdateGroupPlayersRequest;
import com.project.asidesappbe.repositories.GroupRepository;
import com.project.asidesappbe.repositories.PlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerService playerService;

    public ResponseEntity<String> createGroupAndSaveIdToPlayer(@Valid CreateGroupRequest groupToCreate) {
        Player playerToUpdate = playerRepository.findBy_playerId(groupToCreate.get_playerId());
//        Save Group to repo
        Group savedGroup = saveNewGroup(
            new Group(
                    groupToCreate.getGroupName(),
                    playerToUpdate.get_playerId()
            )
        );
//        Get groupId and set to player
        addGroupIdToCreatingPlayer(savedGroup, playerToUpdate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedGroup.toString());
    }
//     Is this needed??
    private Group saveNewGroup(Group groupToSave) {
        return groupRepository.save(groupToSave);
    }
//    Should this return something??
    private void addGroupIdToCreatingPlayer (Group newGroup, Player playerToUpdate) {
        playerToUpdate.set_groupId(newGroup.get_groupId());
        playerRepository.save(playerToUpdate);
    }

    public ResponseEntity<String> addPlayerToGroup(@Valid UpdateGroupPlayersRequest request) {

        Group groupToUpdateList = groupRepository.findBy_groupId(request.get_groupId());
        ObjectId playerId = playerService.addGroupIdToPlayer(request.get_playerId(), groupToUpdateList.get_groupId());
        groupToUpdateList.addPlayer(playerId);
        groupRepository.save(groupToUpdateList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupToUpdateList.toString());
    }

    public ResponseEntity<String> removePlayerfromGroup(@Valid UpdateGroupPlayersRequest request) {

        Group groupToUpdateList = groupRepository.findBy_groupId(request.get_groupId());
        ObjectId playerId = playerService.removeGroupIdFromPlayer(request.get_playerId());
        groupToUpdateList.removePlayer(playerId);
        groupRepository.save(groupToUpdateList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupToUpdateList.toString());
    }

    public ResponseEntity<String> removeGroup(String _groupId) {
        Group groupToRemove = groupRepository.findBy_groupId(_groupId);

        removeDeletedGroupIdFromAllPlayers(groupToRemove);

        groupRepository.deleteById(groupToRemove.get_groupId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Group deleted.");
    }
//    Should this return something??
    private void removeDeletedGroupIdFromAllPlayers(Group groupToRemove) {
        for (ObjectId playerId : groupToRemove.getPlayers()) {
            playerService.removeGroupIdFromPlayer(playerId.toString());
        }
    }
}
