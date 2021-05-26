package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class LogoutController {

    @RequestMapping(value = RouteConstants.LOGOUT_ENDPOINT, method = RequestMethod.POST)
    public ResponseEntity<String> logout(@Valid @RequestBody String jwt) {

//        Invalidate JWT?

        return ResponseEntity.status(HttpStatus.OK).body("Logout Successful");
    }
}
