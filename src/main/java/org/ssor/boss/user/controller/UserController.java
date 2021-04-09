package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.dto.CreateUserInputDTO;
import org.ssor.boss.user.dto.CreateUserResultDTO;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.dto.UserProfileDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.UserAlreadyExistsException;
import org.ssor.boss.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = UserController.USERS_ROUTE)
public class UserController {
	public static final String USERS_ROUTE = "/api/v1/users";
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(produces = { APPLICATION_JSON_VALUE })
	public Iterable<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping(produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE }, consumes = { APPLICATION_JSON_VALUE,
			APPLICATION_XML_VALUE })
	public ResponseEntity<Object> addNewUser(@RequestBody CreateUserInputDTO createUserInputDTO) {
		CreateUserResultDTO userResultDTO;
		try {
			userResultDTO = userService.createUser(createUserInputDTO, LocalDateTime.now());
		} catch (IllegalArgumentException iae) {
			// Bad request.
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (UserAlreadyExistsException uaee) {
			return new ResponseEntity<>(uaee.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(userResultDTO, HttpStatus.CREATED);
	}

	@GetMapping(path = "/{user_id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> getUserById(@PathVariable("user_id") Integer userId) {
		Optional<UserInfoDto> userInfoDto = userService.findUserById(userId);
		if (userInfoDto.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(userInfoDto.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@PutMapping(path = "/{user_id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateUserProfile(@PathVariable("user_id") Integer userId,
			@Valid @RequestBody UserProfileDto userDto) {
		Optional<UserProfileDto> userProfileDto = userService.updateUserProfile(userId, userDto);
		if (userProfileDto.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("User profile updated.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@RequestMapping(path = "/**", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, method = { RequestMethod.GET, RequestMethod.PUT })
	public ResponseEntity<String> notFoundPath() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found.");
	}
}
