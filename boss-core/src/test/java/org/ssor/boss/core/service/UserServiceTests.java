package org.ssor.boss.core.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.entity.UserType;
import org.ssor.boss.core.exception.NoSuchUserException;
import org.ssor.boss.core.exception.UserAlreadyExistsException;
import org.ssor.boss.core.repository.UserRepository;
import org.ssor.boss.core.transfer.RegisterUserInput;
import org.ssor.boss.core.transfer.RegisterUserOutput;
import org.ssor.boss.core.transfer.SecureUserDetails;
import org.ssor.boss.core.transfer.UpdateUserInput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests
{
  static final String FAKE_PASSWORD = "D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E";

  @Mock
  static UserRepository userRepository;
  static List<User> mockUsers;
  static UserService userService;

  @BeforeEach
  void setUp()
  {
    // Populate fake users.
    final var created = LocalDateTime.now();
    final var user1 = new User(1, UserType.USER_DEFAULT, 1, "SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD, created, null, true, false);
    final var user2 = new User(2, UserType.USER_DEFAULT, 1, "Monkey", "monkey@gmail.com", FAKE_PASSWORD, created, null, true, false);
    final var user3 = new User(3, UserType.USER_DEFAULT, 1, "Fish", "fish@gmail.com", FAKE_PASSWORD, created, null, true, false);
    final var user4 = new User(4, UserType.USER_DEFAULT, 1, "LeftRuleMatters", "john.christman@smoothstack.com", FAKE_PASSWORD, created, null, true, false);

    mockUsers = Lists.newArrayList(user1, user2, user3, user4);
    userService = new UserService(userRepository);
    assertThat(userService).isNotNull();
  }

  @AfterEach
  void tearDown()
  {
    mockUsers = null;
    userService = null;
  }

  @Test
  void test_LoadUserByUsernameOrEmail_LoadsUserSuccessfully()
  {
    final var skUsername = "SoraKatadzuma";
    final var fishEmail = "fish@gmail.com";
    final var fsUsername = "Fish";
    doReturn(Optional.of(mockUsers.get(0))).when(userRepository)
                                           .getUserByUsernameOrEmail(eq(skUsername), eq(skUsername));
    doReturn(Optional.of(mockUsers.get(2))).when(userRepository)
                                           .getUserByUsernameOrEmail(eq(fishEmail), eq(fishEmail));

    var retrieved = userService.loadUserByUsername(skUsername);
    verify(userRepository).getUserByUsernameOrEmail(skUsername, skUsername);
    assertThat(retrieved.getUsername()).isEqualTo(skUsername);
    assertThat(retrieved.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(retrieved.isEnabled()).isTrue();

    retrieved = userService.loadUserByUsername(fishEmail);
    verify(userRepository).getUserByUsernameOrEmail(fishEmail, fishEmail);
    assertThat(retrieved.getUsername()).isEqualTo(fsUsername);
    assertThat(retrieved.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(retrieved.isEnabled()).isTrue();
  }

  @Test
  void test_LoadUserByUsernameOrEmail_ThrowsUsernameNotFoundException()
  {
    doReturn(Optional.empty()).when(userRepository).getUserByUsernameOrEmail(any(), any());
    assertThatThrownBy(() -> userService.loadUserByUsername("")).isInstanceOf(UsernameNotFoundException.class)
                                                                .hasMessage("User with username or email not found");
    verify(userRepository).getUserByUsernameOrEmail(any(), any());
  }

  @Test
  void test_GetAllUsersUnsecure_GetsAllUsers()
  {
    doReturn(mockUsers).when(userRepository).findAll();

    final var users = userService.getAllUsersUnsecure();
    verify(userRepository).findAll();
    Assertions.assertThat(users).isEqualTo(mockUsers);
  }

  @Test
  void test_GetAllUsersSecure_GetsAllUsers()
  {
    final var secureUsers = mockUsers.stream().map(SecureUserDetails::new).collect(Collectors.toUnmodifiableList());
    doReturn(mockUsers).when(userRepository).findAll();

    final var users = userService.getAllUsersSecure();
    verify(userRepository).findAll();
    assertThat(users).isEqualTo(secureUsers);
  }

  @Test
  void test_RegisterNewUser_RegistersUserSuccessfully()
  {
    final var username = "Nobody";
    final var email = "you@me.com";
    final var input = new RegisterUserInput(username, email, FAKE_PASSWORD);
    final var created = LocalDateTime.now();
    final var newUser = new User(null, UserType.USER_DEFAULT, 1, username, email, FAKE_PASSWORD, created, null, false, false);
    final var registered = new User(5, UserType.USER_DEFAULT, 1, username, email, FAKE_PASSWORD, created, null, false, false);
    final var output = new RegisterUserOutput(5, UserType.USER_DEFAULT.index(), 1, username, email, created);
    doAnswer(invocationOnMock -> {
      mockUsers.add(registered);
      return registered;
    }).when(userRepository).save(eq(newUser));

    final var captor = ArgumentCaptor.forClass(User.class);
    final var result = userService.registerNewUser(input, created);
    verify(userRepository).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(newUser);
    assertThat(result).isEqualTo(output);
  }

  @Test
  void test_RegisterNewUser_ThrowsUserAlreadyExists_WithExistingUserUsernameOrEmail()
  {
    final var username = "SoraKatadzuma";
    final var email = "sorakatadzuma@gmail.com";
    final var input = new RegisterUserInput(username, email, FAKE_PASSWORD);
    final var created = LocalDateTime.now();
    doReturn(true).when(userRepository).existsUserByUsernameOrEmail(eq(username), eq(email));

    final var usernameCaptor = ArgumentCaptor.forClass(String.class);
    final var emailCaptor = ArgumentCaptor.forClass(String.class);
    assertThatThrownBy(() -> userService.registerNewUser(input, created))
      .isInstanceOf(UserAlreadyExistsException.class).hasMessage("User with username or email already exists");

    verify(userRepository).existsUserByUsernameOrEmail(usernameCaptor.capture(), emailCaptor.capture());
    assertThat(usernameCaptor.getValue()).isEqualTo(username);
    assertThat(emailCaptor.getValue()).isEqualTo(email);
    verify(userRepository, never()).save(any());
  }

  @Test
  void test_UpdateUserProfile_UpdatesUserProfileSuccessfully()
  {
    final var updateInput = new UpdateUserInput(1, "Manchico", "man.chico@gmail.com", FAKE_PASSWORD);
    final var getUser = mockUsers.get(0);
    doReturn(Optional.of(getUser)).when(userRepository).findById(eq(1));

    final var captor = ArgumentCaptor.forClass(int.class);
    final var result = userService.updateUserProfile(updateInput);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(1);
    assertThat(result).isEqualTo("User profile updated");
  }

  @Test
  void test_UpdateUserProfile_ThrowsNoSuchUserException_WithInvalidId()
  {
    final var updateInput = new UpdateUserInput(5, "", "", "");
    doReturn(Optional.empty()).when(userRepository).findById(eq(5));

    final var captor = ArgumentCaptor.forClass(int.class);
    assertThatThrownBy(() -> userService.updateUserProfile(updateInput)).isInstanceOf(NoSuchUserException.class)
                                                                        .hasMessage("No such user with id 5");
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(5);
  }

  @Test
  void test_GetUnsecuredUserDetailsWithId_GetsUserSuccessfully()
  {
    final var user = mockUsers.get(0);
    doReturn(Optional.of(user)).when(userRepository).findById(eq(1));

    final var captor = ArgumentCaptor.forClass(int.class);
    final var result = userService.getUnsecureUserDetailsWithId(1);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(1);
    assertThat(result.isPresent()).isTrue();
    Assertions.assertThat(result.get()).isEqualTo(user);
  }

  @Test
  void test_GetSecuredUserDetailsWithId_GetsUsersSuccessfully()
  {
    final var user = mockUsers.get(0);
    final var secUser = new SecureUserDetails(user);
    doReturn(Optional.of(user)).when(userRepository).findById(eq(1));

    final var captor = ArgumentCaptor.forClass(int.class);
    final var result = userService.getSecureUserDetailsWithId(1);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(1);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo(secUser);
  }

  @Test
  void test_GetSecuredUserDetailsWithId_GetsEmptySuccessfully_WhenUserDoesntExist()
  {
    doReturn(Optional.empty()).when(userRepository).findById(any());

    final var result = userService.getSecureUserDetailsWithId(5);
    verify(userRepository).findById(5);
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void test_PermanentlyDeleteUserWithId_DeletesUserSuccessfully()
  {
    doAnswer(invocationOnMock -> mockUsers.remove(3)).when(userRepository).deleteById(4);
    userService.permanentlyDeleteUserWithId(4);
    verify(userRepository).deleteById(4);
    assertThat(mockUsers.size()).isEqualTo(3);
  }

  @Test
  void test_DeleteUserWithId_DeleteUserSuccessfully()
  {
    final var deleted = LocalDateTime.now();
    final var user = mockUsers.get(0);
    doReturn(Optional.of(user)).when(userRepository).findById(1);
    doAnswer(invocationOnMock -> {
      mockUsers.remove(user);
      user.setDeleted(deleted);
      mockUsers.add(user);
      return user;
    }).when(userRepository).save(any()); // We really don't care what the input was.

    userService.deleteUserWithId(1);
    verify(userRepository).findById(1);
    verify(userRepository).save(any());
    assertThat(mockUsers).contains(user);
  }

  @Test
  void test_DeleteUserWithId_DoesNothingWithInvalidId()
  {
    doReturn(Optional.empty()).when(userRepository).findById(eq(5));
    userService.deleteUserWithId(5);

    final var captor = ArgumentCaptor.forClass(int.class);
    verify(userRepository).findById(captor.capture());
    assertThat(captor.getValue()).isEqualTo(5);
  }
}
