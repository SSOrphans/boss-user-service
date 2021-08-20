package org.ssor.boss.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ssor.boss.core.service.AwsSesService;
import org.ssor.boss.core.service.ConfirmationService;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.*;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UserSettingsInput;
import org.ssor.boss.user.service.ControllerService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_XML_VALUE;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = UserController.USERS_ROUTE)
@CrossOrigin
public class UserController
{
  public static final String USERS_ROUTE = "/api/v1/users";
  private final UserService userService;
  private final AwsSesService awsSesService;
  private final ConfirmationService confirmationService;
  private final ControllerService controllerService;

  @GetMapping
  public ResponseEntity<List<SecureUserDetails>> getAllUsers()
  {
    return ResponseEntity.ok(userService.getAllUsersSecure());
  }

  @PostMapping("/registration")
  public ResponseEntity<RegisterUserOutput> registerNewUser(@Valid @RequestBody RegisterUserInput registerUserInput)
  {
    final var newUser = userService.registerNewUser(registerUserInput, Instant.now());
    //final var generationOutput = confirmationService.generateConfirmation(USER_CONFIRMATION, newUser.getId());
    // Send email using confirmation generation output.
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
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
    var optionalUser = userService.getSecureUserDetailsWithId(userId);
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

  @GetMapping("/{user_id}/profile")
  public ResponseEntity<Object> getUserProfileInfo(@PathVariable("user_id") int userId, HttpServletRequest request)
  {
    var userProfile = controllerService.getUserInfo(userId);
    if (userProfile.isPresent())
      return ResponseEntity.ok(userProfile.get());

    final var response = new ApiRequestResponse();
    response.setTimestamp(Instant.now().toEpochMilli());
    response.setStatus(NOT_FOUND.value());
    response.setReason(NOT_FOUND.getReasonPhrase());
    response.setMessage(String.format("User with id %d was not found", userId));
    response.setPath(request.getServletPath());
    response.setValidationErrors(new HashMap<>());
    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @PutMapping(path ="/{user_id}",consumes = { APPLICATION_JSON_VALUE,
          APPLICATION_XML_VALUE })
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

  @PutMapping(path ="/{user_id}/settings",consumes = { APPLICATION_JSON_VALUE,
          APPLICATION_XML_VALUE })
  public ResponseEntity<ApiRequestResponse>
  updateUserSettings(@PathVariable("user_id") int userId, @Valid @RequestBody UserSettingsInput userSettingsInput,
                    HttpServletRequest request)
  {
    if (!controllerService.updateUserSettings(userId,userSettingsInput))
      return notFoundResponse(request, String.format("User with id %d not found", userId));

    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @DeleteMapping("/{user_id}")
  public ResponseEntity<ApiRequestResponse>
  deleteUserWithId(@PathVariable("user_id") int userId, HttpServletRequest request)
  {
    final var optionalUser = userService.getSecureUserDetailsWithId(userId);
    if (optionalUser.isEmpty())
      return notFoundResponse(request, String.format("User with id %d not found", userId));

    userService.deleteUserWithId(userId);
    return okResponse(request, String.format("User with id %d deleted", userId));
  }

  @PostMapping(path = "/forgot-password", consumes = { APPLICATION_JSON_VALUE,
                                                         APPLICATION_XML_VALUE })
  public ResponseEntity<String> forgotPasswordEmail(
      @Valid @RequestBody ForgotPassEmailInput forgotPasswordEmailInput) {
    Optional<Email> optEmail = controllerService.sendPasswordReset(forgotPasswordEmailInput);
    if(optEmail.isEmpty()){
      return new ResponseEntity<>(null,HttpStatus.ACCEPTED);
    }
    awsSesService.sendEmail(optEmail.get());
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }

  @PutMapping(path = "/reset-password", consumes = { APPLICATION_JSON_VALUE,
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
    return ResponseEntity.status(response.getStatus()).body(response);
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