/**
 * 
 */
package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.dto.RetrieveUserDto;
import org.ssor.boss.user.service.RetrieveUserService;

/**
 * @author Christian Angeles
 *
 */
@RestController
public class RetrieveUserController {

	@Autowired
	RetrieveUserService userService;

	@GetMapping(path = "/api/v1/users/{user_id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getUserById(@PathVariable("user_id") Integer userId) {
		RetrieveUserDto userInfoDto = userService.findUserById(userId);
		if (userInfoDto != null) {
			return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
		}
		return ResponseEntity.status(HttpStatus.OK).body("User does not exist.");
	}

	@RequestMapping(path = "/**", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<String> notFoundPath() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found.");
	}
}
