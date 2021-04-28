package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.exception.UserAlreadyExistsException;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.RegisterUserInput;
import org.ssor.boss.core.transfer.RegisterUserOutput;
import org.ssor.boss.core.transfer.UpdateUserInput;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UserInfoOutput;
import org.ssor.boss.user.service.ControllerService;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@CrossOrigin
public class UserController {
	public static final String USERS_ROUTE = "/api/v1/users";

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	ControllerService controllerService;

	@GetMapping(value = USERS_ROUTE)
	public Iterable<User> getAllUsers() {
		return userService.getAllUsersUnsecure();
	}

	@PostMapping(value = USERS_ROUTE, consumes = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
	public ResponseEntity<Object> addNewUser(@RequestBody RegisterUserInput registerUserInput) {
		RegisterUserOutput registerUserOutput;
		try {
			registerUserOutput = userService.registerNewUser(registerUserInput, LocalDateTime.now());
		} catch (IllegalArgumentException iae) {
			// Bad request.
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (UserAlreadyExistsException uaee) {
			return new ResponseEntity<>(uaee.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(registerUserOutput, HttpStatus.CREATED);
	}

	@GetMapping(path = USERS_ROUTE + "/{user_id}")
	public ResponseEntity<Object> getUserInfo(@PathVariable("user_id") Integer userId) {
		Optional<UserInfoOutput> userInfoOutput = controllerService.getUserInfo(userId);
		if (userInfoOutput.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(userInfoOutput.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@PutMapping(path = USERS_ROUTE + "/{user_id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateUserProfile(@PathVariable("user_id") Integer userId,
			@Valid @RequestBody UpdateUserInput updateUserInput) {
		if (controllerService.updateUserProfile(userId, updateUserInput)) {
			return ResponseEntity.status(HttpStatus.OK).body("User profile updated.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@DeleteMapping(path = USERS_ROUTE + "/{user_id}")
	public ResponseEntity<String> deleteUserAccount(@PathVariable("user_id") Integer userId) {
		if (controllerService.deleteUserAccount(userId)) {
			return ResponseEntity.status(HttpStatus.OK).body("User account deleted.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
	}

	@PostMapping(path = "/api/v1/user/email", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> forgotPasswordEmail(
			@Valid @RequestBody ForgotPassEmailInput forgotPasswordEmailInput) {
		controllerService.sendPasswordReset(forgotPasswordEmailInput);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password reset link sent to email.");
	}

	@PutMapping(path = "/api/v1/user/password", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> updateForgotPassword(
			@Valid @RequestBody ForgotPassTokenInput forgotPasswordTokenInput) {
		if (controllerService.updateForgotPassword(forgotPasswordTokenInput)) {
			return ResponseEntity.status(HttpStatus.OK).body("User password updated.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is was an issue updating the password.");
	}
}
