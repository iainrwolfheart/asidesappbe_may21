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
    public ResponseEntity<String> addOrRemovePlayerIdInGroupObject(@Valid UpdateGroupPlayersRequest request) {
//      Handle fringe case of _groupId not found -> NullPointerException

//      If player already assigned a _groupId, they are not assigned to a new group...

        Group groupToUpdateList = groupRepository.findBy_groupId(request.get_groupId());
        Player playerToUpdate = playerRepository.findBy_playerId(request.get_playerId());

        groupToUpdateList.addOrRemovePlayer(playerToUpdate.get_playerId());
        playerToUpdate.addOrRemoveGroupId(groupToUpdateList.get_groupId());

        groupRepository.save(groupToUpdateList);
        playerRepository.save(playerToUpdate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupToUpdateList.toString());
    }
//    Well, this is expensive. Must be a better way...
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
            Player playerToRemoveGroupId = playerRepository.findBy_playerId(playerId);
            playerToRemoveGroupId.set_groupId(null);
            playerRepository.save(playerToRemoveGroupId);
        }
    }
}
