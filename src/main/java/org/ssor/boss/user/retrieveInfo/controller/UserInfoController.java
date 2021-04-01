/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.service.UserInfoService;


/**
 * @author Christian Angeles
 *
 */
@RestController
@RequestMapping(path = "/api/v1")
public class UserInfoController {

	@Autowired
	UserInfoService userService;
	
	@RequestMapping(path = "/users/{user_id}", 
					produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, 
					method = RequestMethod.GET)
	public UserInfoDto getUserById(@PathVariable("user_id") Integer userId) {
		return userService.findUserById(userId);
	}
}
