package org.ssor.boss.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.entity.UserType;
import org.ssor.boss.exception.UserAlreadyExistsException;
import org.ssor.boss.repository.UserEntityRepository;
import org.ssor.boss.dto.CreateUserInputDTO;
import org.ssor.boss.dto.CreateUserResultDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests
{
  @Mock
  static UserEntityRepository userEntityRepository;
  static ArrayList<UserEntity> users;
  static UserService userService;

  @BeforeEach
  void setup()
  {
    final var typeId = UserType.DEFAULT.ordinal();
    final var user1 = new UserEntity(1, typeId, 1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    final var user2 = new UserEntity(2, typeId, 1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    final var user3 = new UserEntity(3, typeId, 1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    final var user4 = new UserEntity(4, typeId, 1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
    users = Lists.newArrayList(user1, user2, user3, user4);
    userService = new UserService(userEntityRepository);
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
    when(userEntityRepository.findAll()).thenReturn(users);

    final var retrieved = userService.getAllUsers();
    assertThat(retrieved).isEqualTo(users);
    verify(userEntityRepository).findAll();
  }

  @Test
  void test_CanCreateUserFromInputWithoutError()
  {
    final var input = new CreateUserInputDTO("monkey", "me@example.com", "password");
    final var createdAt = LocalDateTime.now();
    final var typeId = UserType.DEFAULT.ordinal();
    final var postUser = new UserEntity(null, typeId, 1, input.getDisplayName(), input.getEmail(), input.getPassword(), createdAt, null, false);
    final var returnedUser = new UserEntity(5, typeId, 1, input.getDisplayName(), input.getEmail(), input.getPassword(), createdAt, null, false);
    final var output = new CreateUserResultDTO(5, typeId, 1, input.getDisplayName(), input.getEmail(), createdAt);
    doAnswer(invocation -> {
      users.add(returnedUser);
      return returnedUser;
    }).when(userEntityRepository).save(postUser);

    final var captor = ArgumentCaptor.forClass(UserEntity.class);
    final var result = userService.createUser(input, createdAt);
    verify(userEntityRepository).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(postUser);
    assertThat(result).isEqualTo(output);
  }

  @Test
  void test_CanCreateUserFromInputThrowsIAEOnNullDTO()
  {
    assertThatThrownBy(() -> userService.createUser(null, LocalDateTime.now()))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_DTO_MESSAGE);
    verify(userEntityRepository, never()).checkUserExistsWithUsernameAndEmail(any(), any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanCreateUserFromInputThrowsIAEOnNullLDT()
  {
    final var input = new CreateUserInputDTO("monkey", "me@example.com", "password");
    assertThatThrownBy(() -> userService.createUser(input, null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_LDT_MESSAGE);
    verify(userEntityRepository, never()).checkUserExistsWithUsernameAndEmail(any(), any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanCreateUserFromInputThrowsIAEOnNullUsername()
  {
    final var input = new CreateUserInputDTO(null, "me@example.com", "password");
    assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_USERNAME_MESSAGE);
    verify(userEntityRepository, never()).checkUserExistsWithUsernameAndEmail(any(), any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanCreateUserFromInputThrowsIAEOnNullEmail()
  {
    final var input = new CreateUserInputDTO("monkey", null, "password");
    assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_EMAIL_MESSAGE);
    verify(userEntityRepository, never()).checkUserExistsWithUsernameAndEmail(any(), any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanCreateUserFromInputThrowsIAEOnNullPassword()
  {
    final var input = new CreateUserInputDTO("monkey", "me@example.com", null);
    assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_PASSWORD_MESSAGE);
    verify(userEntityRepository, never()).checkUserExistsWithUsernameAndEmail(any(), any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CannotCreateUserFromInputBecauseUserIsTaken()
  {
    final var input = new CreateUserInputDTO("username", "me@example.com", "password");

    // We're forcing the mock to return true so the code path gets executed.
    doReturn(true).when(userEntityRepository).checkUserExistsWithUsernameAndEmail(any(), any());

    assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
      .isInstanceOf(UserAlreadyExistsException.class)
      .hasMessage(UserAlreadyExistsException.USER_TAKEN_MESSAGE);
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanFilterUserFromRepositoryWithValidRetrieval()
  {
    final var user = users.get(0);
    final var optionalUser = Optional.ofNullable(user);
    doReturn(optionalUser).when(userEntityRepository).findById(1);

    final var retrieved = userService.getUserWithId(1);
    final var captor = ArgumentCaptor.forClass(Integer.class);
    verify(userEntityRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(1);
    assertThat(retrieved).isEqualTo(optionalUser);
  }

  @Test
  void test_CannotFilterUserFromRepositoryWithInvalidRetrieval()
  {
    doReturn(Optional.empty()).when(userEntityRepository).findById(5);

    final var retrieved = userService.getUserWithId(5);
    final var captor = ArgumentCaptor.forClass(Integer.class);
    verify(userEntityRepository).findById(captor.capture());
    verify(userEntityRepository, never()).save(any());
    assertThat(captor.getValue()).isEqualTo(5);
    assertThat(retrieved).isEqualTo(Optional.empty());
  }

  @Test
  void test_CanUpdateUserFromRepositoryThrowsIAEWithNullDTO()
  {
    assertThatThrownBy(() -> userService.updateUserWithId(1, null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.NULL_DTO_MESSAGE);
    verify(userEntityRepository, never()).findById(any());
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanUpdateUserFromRepositoryThrowsIAEWithInvalidId()
  {
    doReturn(Optional.empty()).when(userEntityRepository).findById(5);

    final var userDTO = new CreateUserInputDTO("monkey", "me@example.com", "newPassword");
    final var captor = ArgumentCaptor.forClass(Integer.class);
    assertThatThrownBy(() -> userService.updateUserWithId(5, userDTO))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(UserService.INVALID_USER_ID + 5);
    verify(userEntityRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(5);
    verify(userEntityRepository, never()).save(any());
  }

  @Test
  void test_CanUpdateUserFromRepositoryWithValidId()
  {
    final var userDTO = new CreateUserInputDTO("monkey", "me@example.com", "newPassword");
    final var originalUser = Optional.of(users.get(0));
    final var updatedUser = users.get(0);
    updatedUser.setDisplayName(userDTO.getDisplayName());
    updatedUser.setEmail(userDTO.getEmail());
    updatedUser.setPassword(userDTO.getPassword());
    doReturn(originalUser).when(userEntityRepository).findById(1);

    final var captor = ArgumentCaptor.forClass(UserEntity.class);
    userService.updateUserWithId(1, userDTO);
    verify(userEntityRepository).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(updatedUser);
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
    }).when(userEntityRepository).deleteById(1);

    userService.deleteUserWithId(1);
    assertThat(users.get(0).getId()).isEqualTo(2);
    verify(userEntityRepository).deleteById(1);
  }
}
