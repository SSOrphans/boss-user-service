/**
 * 
 */
package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.dto.UpdateUserDto;
import org.ssor.boss.user.service.UpdateUserService;

/**
 * @author Christian Angeles
 *
 */
@RestController
public class UpdateUserController {

	@Autowired
	UpdateUserService userProfileService;

	@PutMapping(path = "/api/v1/users/{user_id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateUserProfile(@PathVariable("user_id") Integer userId,
			@RequestBody UpdateUserDto userProfileDto) {
		return userProfileService.updateUserProfile(userId, userProfileDto);
	}

	@PutMapping(path = "/**", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> notFoundPath() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found.");
	}
}
