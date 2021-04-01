package org.ssor.boss.user.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ssor.boss.user.dto.UserDTO;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests
{
  @Mock
  static UserRepository userRepository;
  static ArrayList<UserEntity> users;
  static UserService userService;

  @BeforeEach
  void setup()
  {
    var user1 = new UserEntity(1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user2 = new UserEntity(2, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user3 = new UserEntity(3, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    var user4 = new UserEntity(4, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    users = Lists.newArrayList(user1, user2, user3, user4);
    userService = new UserService(userRepository);
    assertThat(userService).isNotNull();
  }

  @AfterEach
  void teardown()
  {
    users = null;
    userService = null;
  }

  @Test
  void test_CanGetAllUsersFromRepository()
  {
    when(userRepository.findAll()).thenReturn(users);

    final var retrieved = userService.getAllUsers();
    verify(userRepository).findAll();
    assertThat(retrieved).isEqualTo(users);
  }

  @Test
  void test_CanFilterUserFromRepositoryWithValidRetrieval()
  {
    final var user = users.get(0);
    doReturn(Optional.ofNullable(user)).when(userRepository).findById(1);

    final var retrieved = userService.getUserWithId(1);
    final var captor = ArgumentCaptor.forClass(Integer.class);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(1);
    assertThat(retrieved).isEqualTo(user);
  }

  @Test
  void test_CannotFilterUserFromRepositoryWithInvalidRetrieval()
  {
    doReturn(Optional.ofNullable(null)).when(userRepository).findById(5);

    final var retrieved = userService.getUserWithId(5);
    final var captor = ArgumentCaptor.forClass(Integer.class);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(5);
    assertThat(retrieved).isNull();
  }

  @Test
  void test_CanUpdateUserFromRepositoryThrowsNullPointerException()
  {
    final var result = Optional.ofNullable(users.get(0));
    doReturn(result).when(userRepository).findById(1);
    assertThatThrownBy(() -> userService.updateUserWithId(1, null))
      .isInstanceOf(NullPointerException.class);
  }

  @Test
  void test_CanUpdateUserFromRepositoryWithValidId()
  {
    final var userDTO = new UserDTO("Monkey", "john.christman@smoothstack.com", "newPassword");
    final var originalUser = Optional.of(users.get(0));
    final var updatedUser = users.get(0);
    updatedUser.setDisplayName(userDTO.getDisplayName());
    updatedUser.setEmail(userDTO.getEmail());
    updatedUser.setPassword(userDTO.getPassword());
    doReturn(originalUser).when(userRepository).findById(1);

    final var captor = ArgumentCaptor.forClass(UserEntity.class);
    userService.updateUserWithId(1, userDTO);
    verify(userRepository).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(updatedUser);
  }

  @Test
  void test_CannotUpdateUserFromRepositoryWithInvalidId()
  {
    final var userDTO = new UserDTO("Monkey", "john.christman@smoothstack.com", "newPassword");
    doReturn(Optional.ofNullable(null)).when(userRepository).findById(5);

    final var captor = ArgumentCaptor.forClass(Integer.class);
    assertThatThrownBy(() -> userService.updateUserWithId(5, userDTO))
      .isInstanceOf(IllegalArgumentException.class).hasMessage("No such user with id: 5");
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(5);
  }

  @Test
  void test_CanDeleteUserFromRepository()
  {
    final var updatedUser = users.get(0);
    final var deletedTime = LocalDateTime.now();
    updatedUser.setDeleted(deletedTime);
    doAnswer(invocation -> {
      final var arg1 = invocation.getArgument(0, Integer.class);
      users.remove(0);
      return null;
    }).when(userRepository).deleteById(1);

    userService.deleteUserWithId(1);
    verify(userRepository).deleteById(1);
    assertThat(users.get(0).getId()).isEqualTo(2);
  }
}
