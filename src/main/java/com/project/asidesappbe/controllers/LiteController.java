package com.project.asidesappbe.controllers;

import com.project.asidesappbe.constants.RouteConstants;
import com.project.asidesappbe.services.TeamsGeneratorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LiteController {

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = RouteConstants.LITE_ENDPOINT, method = RequestMethod.POST)
	public ResponseEntity<Object> receiveAvailablePlayers(@RequestBody String request) {
		
		JSONObject requestObject = new JSONObject(request);
		JSONArray playersJSON = requestObject.getJSONArray("players");
		
		TeamsGeneratorService service = new TeamsGeneratorService();
		JSONObject teams = service.generateLiteTeams(playersJSON);
		
		System.out.println(teams.toString());
		
		return ResponseEntity.status(HttpStatus.OK).body(teams.toString());
		
	}
}
