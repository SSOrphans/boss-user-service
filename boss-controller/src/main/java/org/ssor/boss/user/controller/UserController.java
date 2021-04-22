package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.core.dtos.CreateUserInputDTO;
import org.ssor.boss.core.dtos.CreateUserResultDTO;
import org.ssor.boss.core.dtos.ForgotPassEmailDto;
import org.ssor.boss.core.dtos.ForgotPassTokenDto;
import org.ssor.boss.core.dtos.UserInfoDto;
import org.ssor.boss.core.dtos.UserProfileDto;
import org.ssor.boss.core.entities.UserEntity;
import org.ssor.boss.core.exceptions.UserAlreadyExistsException;
import org.ssor.boss.core.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class UserController {
	public static final String USERS_ROUTE = "/api/v1/users";
//	private UserService userService;
//
//	@Autowired
//	public void setUserService(UserService userService) {
//		this.userService = userService;
//	}
	
	@Autowired
	UserService userService;

	@GetMapping(value = USERS_ROUTE)
	public Iterable<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping(value = USERS_ROUTE, consumes = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
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

	@GetMapping(path = USERS_ROUTE + "/{user_id}")
	public ResponseEntity<Object> getUserById(@PathVariable("user_id") Integer userId) {
		Optional<UserInfoDto> userInfoDto = userService.findUserById(userId);
		if (userInfoDto.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(userInfoDto.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@PutMapping(path = USERS_ROUTE + "/{user_id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateUserProfile(@PathVariable("user_id") Integer userId,
			@Valid @RequestBody UserProfileDto userDto) {
		Optional<UserProfileDto> userProfileDto = userService.updateUserProfile(userId, userDto);
		if (userProfileDto.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("User profile updated.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@DeleteMapping(path = USERS_ROUTE + "/{user_id}")
	public ResponseEntity<String> deleteUserAccount(@PathVariable("user_id") Integer userId) {
		if (userService.deleteUserAccount(userId)) {
			return ResponseEntity.status(HttpStatus.OK).body("User account deleted.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@PostMapping(path = "/api/v1/user/email", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> forgotPasswordEmail(@Valid @RequestBody ForgotPassEmailDto userForgotPasswordEmailDto) {
		userService.sendPasswordReset(userForgotPasswordEmailDto);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password reset link sent to email.");
	}

	@PutMapping(path = "/api/v1/user/password", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateForgotPassword(@Valid @RequestBody ForgotPassTokenDto userForgotPasswordTokenDto) {
		if(userService.updateForgotPassword(userForgotPasswordTokenDto).isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("User password updated.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User password was not updated.");
	}
}
