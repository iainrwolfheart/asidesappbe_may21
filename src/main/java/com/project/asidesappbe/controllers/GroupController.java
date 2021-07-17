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

    /*
    Returns JSON friendly toString of created group,
    including the invite code and list of _playerIds toString
     */
    @PostMapping(value = RouteConstants.CREATE_ENDPOINT)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> createGroup(@Valid @RequestBody CreateGroupRequest groupToCreate) {
        return groupService.createGroupAndSaveIdToPlayer(groupToCreate);
    }

    /*
    Returns updated group details, including list of _playerIds tostring
     */
    @PutMapping(value = RouteConstants.ADDGROUPPLAYERS)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> addGroupPlayer(
            @Valid @RequestBody UpdateGroupPlayersRequest request) {
        return groupService.addPlayerToGroup(request);
    }

    /*
Returns updated group details, including list of _playerIds tostring
 */
    @PutMapping(value = RouteConstants.REMOVEGROUPPLAYERS)
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> removeGroupPlayer(
            @Valid @RequestBody UpdateGroupPlayersRequest request) {
        return groupService.removePlayerfromGroup(request);
    }

    @GetMapping(value = RouteConstants.GETBYID_ENDPOINT + "/{_groupId}")
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> getGroupById(@Valid @PathVariable String _groupId) {
//        Catch fringe case of unknown _groupId being passed in -> NullPointerException thrown
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupRepository.findBy_groupId(_groupId).toString());
    }

    /*
    Currently one reusable invite code per group.
    Used when a new user is signing up with an invite code shared by another user to retrieve group
    details. When new user confirms to join group, use _groupId in addOrRemove... endpoint.
     */
    @GetMapping(value = RouteConstants.GETGROUPBYINVITECODE_ENDPOINT + "/{inviteCode}")
    @PreAuthorize("hasAnyRole('ROLE_GROUPADMIN', 'ROLE_GROUPPLAYER')")
    ResponseEntity<String> getGroupByInviteCode(@Valid @PathVariable String inviteCode) {
//        Catch less fringe case of unknown inviteCode being passed in -> NullPointerException thrown
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupRepository.findByInviteCode(inviteCode).toString());
    }

//    Delete Group Endpoint needs to remove _groupId from all players
    @DeleteMapping(value = RouteConstants.DELETEBYID_ENDPOINT + "/{_groupId}")
    @PreAuthorize("hasRole('ROLE_GROUPADMIN')")
    ResponseEntity<String> deleteGroup(@Valid @PathVariable String _groupId) {
        return groupService.removeGroup(_groupId);
    }

//    Should all these endpoints be returning group list of players??? Probably not.
}
