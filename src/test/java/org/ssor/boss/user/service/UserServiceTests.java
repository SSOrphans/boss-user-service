package org.ssor.boss.user.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests
{
  @Mock
  static UserRepository userRepository;
  static ArrayList<UserEntity> users;
  static UserService userService;

  @BeforeAll
  static void init()
  {
    var user1 = new UserEntity(1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user2 = new UserEntity(2, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user3 = new UserEntity(3, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user4 = new UserEntity(4, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    users = Lists.newArrayList(user1, user2, user3, user4);
  }

  @BeforeEach
  void setup()
  {
    userService = new UserService(userRepository);
    assertNotNull(userService);
  }

  @AfterEach
  void teardown()
  {
    userService = null;
  }

  @Test
  void test_CanGetAllUsersFromRepository()
  {
    when(userRepository.findAll()).thenReturn(users);

    final var retrieved = userService.getAllUsers();
    verify(userRepository).findAll();
    assertEquals(users, retrieved);
  }

  @Test
  void test_CanFilterUserFromRepository()
  {
    final var user = users.get(1);
    when(userRepository.findById(1)).thenReturn(Optional.of(user));
    when(userRepository.findById(5)).thenReturn(Optional.ofNullable(null));

    final var retrievedValid = userService.getUserWithId(1);
    verify(userRepository).findById(1);
    assertEquals(user, retrievedValid);

    final var retrievedInvalid = userService.getUserWithId(5);
    verify(userRepository).findById(5);
    assertEquals(null, retrievedInvalid);
  }
}
