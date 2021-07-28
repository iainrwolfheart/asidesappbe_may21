package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.models.Requests.CreateGroupRequest;
import com.project.asidesappbe.models.Requests.UpdateGroupPlayersRequest;
import com.project.asidesappbe.repositories.GroupRepository;
import com.project.asidesappbe.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/groups")
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;

    /**
     * @param groupToCreate, comprising required groupName and the String id of the player initiating the call
     * @return Group.toString(), currently including the invite code and list of _playerIds toString
     */
    @PostMapping(value = RouteConstants.CREATE_ENDPOINT)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> createGroup(@Valid @RequestBody CreateGroupRequest groupToCreate) {
        return groupService.createGroupAndSaveIdToPlayer(groupToCreate);
    }

    /**
     * @param request comprising String id of group and String id of player to add
     * @return Group.toString(), currently including the invite code and list of _playerIds toString
     */
    @PutMapping(value = RouteConstants.ADDGROUPPLAYERS)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> addGroupPlayer(
            @Valid @RequestBody UpdateGroupPlayersRequest request) {
        return groupService.addPlayerToGroup(request);
    }

    /**
     * @param request comprising String id of group and String id of player to delete
     * @return Group.toString(), currently including the invite code and list of _playerIds toString
     */
    @PutMapping(value = RouteConstants.REMOVEGROUPPLAYERS)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> removeGroupPlayer(
            @Valid @RequestBody UpdateGroupPlayersRequest request) {
        return groupService.removePlayerfromGroup(request);
    }

    /**
     * @param _groupId as String
     * @return Group.toString()
     */
    @GetMapping(value = RouteConstants.GETBYID_ENDPOINT + "/{_groupId}")
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> getGroupById(@Valid @PathVariable String _groupId) {
//        Catch fringe case of unknown _groupId being passed in -> NullPointerException thrown
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupRepository.findBy_groupId(_groupId).toString());
    }

    /**
     * Used when a player registering has an invite code shared with them.
     * Their reg details are first saved and then this endpoint fetches the group to make use of the
     * addPlayerToGroup endpoint.
     * Currently only one reusable invite code per group. This will be refactored to a limited array per group.
     * @param inviteCode
     * @return Group.toString()
     */
    @GetMapping(value = RouteConstants.GETGROUPBYINVITECODE_ENDPOINT + "/{inviteCode}")
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> getGroupByInviteCode(@Valid @PathVariable String inviteCode) {
//        Catch less fringe case of unknown inviteCode being passed in -> NullPointerException thrown
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupRepository.findByInviteCode(inviteCode).toString());
    }

    /**
     * Endpoint used to delete a group and remove its' ID from it's players.
     * Also replaces any GROUPADMIN_ROLE authorities w/GROUPPLAYER_ROLE authorities.
     * @param _groupId
     * @return NO_CONTENT status
     */
    @DeleteMapping(value = RouteConstants.DELETEBYID_ENDPOINT + "/{_groupId}")
    @PreAuthorize("hasRole('ROLE_GROUPADMIN')")
    ResponseEntity<String> deleteGroup(@Valid @PathVariable String _groupId) {
        return groupService.removeGroup(_groupId);
    }

//    Should all these endpoints be returning group list of players??? Probably not.
}
