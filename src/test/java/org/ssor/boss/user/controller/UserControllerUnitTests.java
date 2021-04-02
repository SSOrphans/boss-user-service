package org.ssor.boss.user.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ssor.boss.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTests
{
  @Mock
  UserService userService;
  UserController userController;

  @BeforeEach
  void setup()
  {
    userController = new UserController();
    userController.setUserService(userService);
  }

  @AfterEach
  void teardown()
  {
    userController = null;
  }
}
