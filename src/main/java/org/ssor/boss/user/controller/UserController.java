package org.ssor.boss.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.user.dto.UserDTO;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.service.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = UserController.USERS_ROUTE)
public class UserController
{
  public static final String USERS_ROUTE = "/api/v1/users";
  private UserService userService;

  @Autowired
  public void setUserRepository(UserService userService)
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
  public void addNewUser(@RequestBody UserDTO userDTO)
  {

  }

  @GetMapping(value = "{user_id}")
  public UserEntity getUserWithId(@RequestParam(name = "user_id") Integer userId)
  {
    return userService.getUserWithId(userId);
  }

  @PutMapping(value = "{user_id}")
  public void patchUserWithId(@RequestParam(name = "user_id") Integer userId, @RequestBody UserDTO userDTO)
  {
    userService.updateUserWithId(userId, userDTO);
  }

  @DeleteMapping(value = "{user_id}")
  public void deleteUserWithId(@RequestParam(name = "user_id") Integer userId)
  {
    userService.deleteUserWithId(userId);
  }
}
