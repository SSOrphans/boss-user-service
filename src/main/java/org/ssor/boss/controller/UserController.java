package org.ssor.boss.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.dto.CreateUserInputDTO;
import org.ssor.boss.dto.CreateUserResultDTO;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.service.UserService;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Slf4j
@Validated
@RestController
@RequestMapping(value = UserController.USERS_ROUTE)
public class UserController
{
  public static final String USERS_ROUTE = "/api/v1/users";
  private UserService userService;

  @Autowired
  public void setUserService(UserService userService)
  {
    this.userService = userService;
  }

  @GetMapping(produces = { APPLICATION_JSON_VALUE })
  public Iterable<UserEntity> getAllUsers()
  {
    return userService.getAllUsers();
  }

  @PostMapping(produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE },
               consumes = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
  public ResponseEntity<CreateUserResultDTO> addNewUser(@Valid @RequestBody CreateUserInputDTO createUserInput)
    throws ConstraintViolationException
  {
    final var result = userService.createUser(createUserInput, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping("{user_id}")
  public void deleteUserWithId(@PathVariable("user_id") Integer userId)
  {
    log.debug("user id = {}", userId);
    userService.deleteUserWithId(userId);
  }
}
