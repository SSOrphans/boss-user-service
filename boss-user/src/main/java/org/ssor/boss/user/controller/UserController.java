//package org.ssor.boss.user.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.ssor.boss.core.entity.User;
//import org.ssor.boss.core.exception.UserAlreadyExistsException;
//import org.ssor.boss.core.service.UserService;
//import org.ssor.boss.core.transfer.RegisterUserInput;
//import org.ssor.boss.core.transfer.RegisterUserOutput;
//import org.ssor.boss.user.dto.ForgotPassEmailInput;
//import org.ssor.boss.user.dto.ForgotPassTokenInput;
//import org.ssor.boss.user.dto.UpdateProfileInput;
//import org.ssor.boss.user.dto.UserInfoOutput;
//import org.ssor.boss.user.service.ControllerService;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import javax.validation.Valid;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
//
//@RestController
//@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
//@CrossOrigin
//public class UserController {
//  public static final String USERS_ROUTE = "/api/v1/users";
//
//  private UserService userService;
//
//  @Autowired
//  public void setUserService(UserService userService) {
//    this.userService = userService;
//  }
//
//  @Autowired
//  ControllerService controllerService;
//
//  @GetMapping(value = USERS_ROUTE)
//  public Iterable<User> getAllUsers() {
//    return userService.getAllUsersUnsecure();
//  }
//
//  @PostMapping(value = USERS_ROUTE, consumes = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
//  public ResponseEntity<Object> addNewUser(@RequestBody RegisterUserInput registerUserInput) {
//    RegisterUserOutput registerUserOutput;
//    try {
//      registerUserOutput = userService.registerNewUser(registerUserInput, Instant.now());
//    } catch (IllegalArgumentException iae) {
//      // Bad request.
//      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//    } catch (UserAlreadyExistsException uaee) {
//      return new ResponseEntity<>(uaee.getMessage(), HttpStatus.CONFLICT);
//    }
//
//    return new ResponseEntity<>(registerUserOutput, HttpStatus.CREATED);
//  }
//
//  @GetMapping(path = USERS_ROUTE + "/{user_id}")
//  public ResponseEntity<Object> getUserInfo(@PathVariable("user_id") Integer userId) {
//    Optional<UserInfoOutput> userInfoOutput = controllerService.getUserInfo(userId);
//    if (userInfoOutput.isPresent()) {
//      return ResponseEntity.status(HttpStatus.OK).body(userInfoOutput.get());
//    }
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
//  }
//
//  @PutMapping(path = USERS_ROUTE + "/{user_id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
//                                                              MediaType.APPLICATION_XML_VALUE })
//  public ResponseEntity<String> updateUserProfile(@PathVariable("user_id") Integer userId,
//                                                  @Valid @RequestBody UpdateProfileInput updateProfileInput) {
//    if (controllerService.updateUserProfile(userId, updateProfileInput)) {
//      return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//    return new ResponseEntity<>("User does not exist.", HttpStatus.NOT_FOUND);
//  }
//
//  @DeleteMapping(path = USERS_ROUTE + "/{user_id}")
//  public ResponseEntity<String> deleteUserAccount(@PathVariable("user_id") Integer userId) {
//    if (controllerService.deleteUserAccount(userId)) {
//      return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
//  }
//
//  @PostMapping(path = "/api/v1/user/email", consumes = { MediaType.APPLICATION_JSON_VALUE,
//                                                         MediaType.APPLICATION_XML_VALUE })
//  public ResponseEntity<String> forgotPasswordEmail(
//      @Valid @RequestBody ForgotPassEmailInput forgotPasswordEmailInput) {
//    controllerService.sendPasswordReset(forgotPasswordEmailInput);
//    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
//  }
//
//  @PutMapping(path = "/api/v1/user/password", consumes = { MediaType.APPLICATION_JSON_VALUE,
//                                                           MediaType.APPLICATION_XML_VALUE })
//  public ResponseEntity<String> updateForgotPassword(
//      @Valid @RequestBody ForgotPassTokenInput forgotPasswordTokenInput) {
//    if (controllerService.updateForgotPassword(forgotPasswordTokenInput)) {
//      return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is was an issue updating the password.");
//  }
//}
package org.ssor.boss.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.core.service.ConfirmationService;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.ApiRequestResponse;
import org.ssor.boss.core.transfer.FinalizeConfirmationInput;
import org.ssor.boss.core.transfer.RegisterUserInput;
import org.ssor.boss.core.transfer.RegisterUserOutput;
import org.ssor.boss.core.transfer.SecureUserDetails;
import org.ssor.boss.core.transfer.UpdateUserInput;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.service.ControllerService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_XML_VALUE;
import static org.ssor.boss.core.entity.ConfirmationType.USER_CONFIRMATION;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = UserController.USERS_ROUTE)
public class UserController
{
  public static final String USERS_ROUTE = "/api/v1/users";
  private final UserService userService;
  private final ConfirmationService confirmationService;
  private final ControllerService controllerService;
  // private final MailService mailService;

  @GetMapping
  public ResponseEntity<List<SecureUserDetails>> getAllUsers()
  {
    return ResponseEntity.ok(userService.getAllUsersSecure());
  }

  @PostMapping("/registration")
  public ResponseEntity<RegisterUserOutput> registerNewUser(@Valid @RequestBody RegisterUserInput registerUserInput)
  {
    final var newUser = userService.registerNewUser(registerUserInput, Instant.now());
    final var generationOutput = confirmationService.generateConfirmation(USER_CONFIRMATION, newUser.getId());
    // Send email using confirmation generation output.
    return ResponseEntity.ok(newUser);
  }

  @PostMapping("/confirmation")
  public ResponseEntity<ApiRequestResponse>
  confirmUserProfile(@RequestBody FinalizeConfirmationInput input, HttpServletRequest request)
  {
    // Confirm.
    final var userId = confirmationService.confirm(input.getConfirmationCode());
    userService.confirmUserWithId(userId);

    // Create response.
    final var response = new ApiRequestResponse();
    response.setTimestamp(Instant.now().toEpochMilli());
    response.setStatus(OK.value());
    response.setReason(OK.getReasonPhrase());
    response.setMessage("User profile confirmed");
    response.setPath(request.getServletPath());
    response.setValidationErrors(new HashMap<>());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{user_id}")
  public ResponseEntity<Object> getUserById(@PathVariable("user_id") int userId, HttpServletRequest request)
  {
    final var optionalUser = userService.getSecureUserDetailsWithId(userId);
    if (optionalUser.isPresent())
      return ResponseEntity.ok(optionalUser.get());

    final var response = new ApiRequestResponse();
    response.setTimestamp(Instant.now().toEpochMilli());
    response.setStatus(NOT_FOUND.value());
    response.setReason(NOT_FOUND.getReasonPhrase());
    response.setMessage(String.format("User with id %d was not found", userId));
    response.setPath(request.getServletPath());
    response.setValidationErrors(new HashMap<>());
    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @PutMapping("/{user_id}")
  public ResponseEntity<ApiRequestResponse>
  updateUserProfile(@PathVariable("user_id") int userId, @Valid @RequestBody UpdateUserInput updateUserInput,
                    HttpServletRequest request)
  {
    final var optionalUser = userService.getSecureUserDetailsWithId(userId);
    if (optionalUser.isEmpty())
      return notFoundResponse(request, String.format("User with id %d not found", userId));

    // User was found.
    updateUserInput.setUserId(userId);
    return okResponse(request, userService.updateUserProfile(updateUserInput));
  }

  @DeleteMapping("/{user_id}")
  public ResponseEntity<ApiRequestResponse>
  deleteUserWithId(@PathVariable("user_id") int userId, HttpServletRequest request)
  {
    userService.deleteUserWithId(userId);
    return okResponse(request, String.format("User with id %d deleted", userId));
  }

  @PostMapping(path = "/api/v1/user/email", consumes = { APPLICATION_JSON_VALUE,
                                                         APPLICATION_XML_VALUE })
  public ResponseEntity<String> forgotPasswordEmail(
      @Valid @RequestBody ForgotPassEmailInput forgotPasswordEmailInput) {
    controllerService.sendPasswordReset(forgotPasswordEmailInput);
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }

  @PutMapping(path = "/api/v1/user/password", consumes = { APPLICATION_JSON_VALUE,
                                                           APPLICATION_XML_VALUE })
  public ResponseEntity<String> updateForgotPassword(
      @Valid @RequestBody ForgotPassTokenInput forgotPasswordTokenInput) {
    if (controllerService.updateForgotPassword(forgotPasswordTokenInput)) {
      return new ResponseEntity<>(null, HttpStatus.OK);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is was an issue updating the password.");
  }

  // TODO:
  //   Move this function and it's extensions to the ApiRequestResponse.
  private ResponseEntity<ApiRequestResponse> getResponse(HttpStatus status, HttpServletRequest request, String message)
  {
    final var response = new ApiRequestResponse();
    response.setTimestamp(Instant.now().toEpochMilli());
    response.setStatus(status.value());
    response.setReason(status.getReasonPhrase());
    response.setMessage(message);
    response.setPath(request.getServletPath());
    response.setValidationErrors(new HashMap<>());
    return ResponseEntity.ok(response);
  }

  private ResponseEntity<ApiRequestResponse> okResponse(HttpServletRequest request, String message)
  {
    return getResponse(HttpStatus.OK, request, message);
  }

  private ResponseEntity<ApiRequestResponse> notFoundResponse(HttpServletRequest request, String message)
  {
    return getResponse(NOT_FOUND, request, message);
  }
}