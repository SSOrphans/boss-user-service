/**
 * 
 */
package org.ssor.boss.user.updateprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.updateprofile.dto.UserProfileDto;
import org.ssor.boss.user.updateprofile.service.UserProfileService;

/**
 * @author Christian Angeles
 *
 */
@RestController
@RequestMapping(path = "/api/v1")
public class UserProfileController {

	@Autowired
	UserProfileService userProfileService;

	@PutMapping(path = "/users/{user_id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void updateUserProfile(@PathVariable("user_id") Integer userId, @RequestBody UserProfileDto userProfileDto) {
		userProfileService.updateUserProfile(userId, userProfileDto);
	}
}
