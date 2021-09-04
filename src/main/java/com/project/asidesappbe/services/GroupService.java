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

        Player player = playerService.givePlayerGroupAdminPrivileges(groupToCreate.get_playerId());

        Group savedGroup = groupRepository.save(
            new Group(
                    groupToCreate.getGroupName(),
                    player.get_playerId()
            )
        );

        playerService.addGroupIdToPlayer(player.get_playerId().toString(), savedGroup.get_groupId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedGroup.toString());
    }

    public ResponseEntity<String> addPlayerToGroup(@Valid UpdateGroupPlayersRequest request) {

        Group groupToUpdateList = loadGroupByIdString(request.get_groupId());
        ObjectId playerId = playerService.addGroupIdToPlayer(request.get_playerId(), groupToUpdateList.get_groupId());
        groupToUpdateList.addPlayer(playerId);
        groupRepository.save(groupToUpdateList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupToUpdateList.toString());
    }

    public ResponseEntity<String> removePlayerfromGroup(@Valid UpdateGroupPlayersRequest request) {

        Group groupToUpdateList = loadGroupByIdString(request.get_groupId());
        ObjectId playerId = playerService.removeGroupIdFromPlayer(request.get_playerId());
        groupToUpdateList.removePlayer(playerId);
        groupRepository.save(groupToUpdateList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupToUpdateList.toString());
    }

    public ResponseEntity<String> removeGroup(String _groupId) {
        Group groupToRemove = loadGroupByIdString(_groupId);

        removeDeletedGroupIdFromAllPlayers(groupToRemove);

        groupRepository.deleteById(groupToRemove.get_groupId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Group deleted.");
    }

    private void removeDeletedGroupIdFromAllPlayers(Group groupToRemove) {
        for (ObjectId playerId : groupToRemove.getPlayers()) {
            playerService.removeGroupIdFromPlayer(playerId.toString());
        }
    }

    private Group loadGroupByIdString(String groupId) {
        return groupRepository.findBy_groupId(groupId.trim());
    }
}
