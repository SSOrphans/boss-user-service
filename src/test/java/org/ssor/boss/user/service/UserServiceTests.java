package org.ssor.boss.user.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.ssor.boss.user.dto.CreateUserInputDTO;
import org.ssor.boss.user.dto.CreateUserResultDTO;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.dto.UserProfileDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.UserAlreadyExistsException;
import org.ssor.boss.user.exception.UserDataAccessException;
import org.ssor.boss.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
	@Mock
	static UserRepository userRepository;
	static ArrayList<UserEntity> users;
	static UserService userService;

	private static UserEntity userEntity;
	private static UserInfoDto userInfoDto;
	private static UserProfileDto userProfileDto;

	@BeforeEach
	void setup() {
		var user1 = new UserEntity(1, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
		var user2 = new UserEntity(2, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
		var user3 = new UserEntity(3, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
		var user4 = new UserEntity(4, "username", "me@example.com", "password", LocalDateTime.now(), null, true);
		users = Lists.newArrayList(user1, user2, user3, user4);
		userService = new UserService(userRepository);
		assertThat(userService).isNotNull();
		
		userEntity = UserEntity.builder().id(1).displayName("Test").email("test@ss.com").password("1234")
				.created(LocalDateTime.now()).deleted(null).confirmed(true).build();
		userInfoDto = UserInfoDto.builder().userId(1).displayName("Test").email("test@ss.com")
				.created(LocalDateTime.now()).build();
		userProfileDto = UserProfileDto.builder().displayName("Test").email("test@ss.com").password("1234").build();
	}

	@AfterEach
	void teardown() {
		users = null;
		userService = null;
	}

	@Test
	void test_CanGetAllUsersFromRepository() {
		when(userRepository.findAll()).thenReturn(users);

		final var retrieved = userService.getAllUsers();
		assertThat(retrieved).isEqualTo(users);
		verify(userRepository).findAll();
	}

	@Test
	void test_CanCreateUserFromInputWithoutError() {
		final var input = new CreateUserInputDTO("monkey", "me@example.com", "password");
		final var createdAt = LocalDateTime.now();
		final var postUser = new UserEntity(null, input.getDisplayName(), input.getEmail(), input.getPassword(),
				createdAt, null, false);
		final var returnedUser = new UserEntity(5, input.getDisplayName(), input.getEmail(), input.getPassword(),
				createdAt, null, false);
		final var output = new CreateUserResultDTO(5, input.getDisplayName(), input.getEmail(), createdAt);
		doAnswer(invocation -> {
			users.add(returnedUser);
			return returnedUser;
		}).when(userRepository).save(postUser);

		final var captor = ArgumentCaptor.forClass(UserEntity.class);
		final var result = userService.createUser(input, createdAt);
		verify(userRepository).save(captor.capture());
		assertThat(captor.getValue()).isEqualTo(postUser);
		assertThat(result).isEqualTo(output);
	}

	@Test
	void test_CanCreateUserFromInputThrowsIAEOnNullDTO() {
		assertThatThrownBy(() -> userService.createUser(null, LocalDateTime.now()))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(UserService.NULL_DTO_MESSAGE);
		verify(userRepository, never()).checkUserExists(any(), any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanCreateUserFromInputThrowsIAEOnNullLDT() {
		final var input = new CreateUserInputDTO("monkey", "me@example.com", "password");
		assertThatThrownBy(() -> userService.createUser(input, null)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(UserService.NULL_LDT_MESSAGE);
		verify(userRepository, never()).checkUserExists(any(), any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanCreateUserFromInputThrowsIAEOnNullUsername() {
		final var input = new CreateUserInputDTO(null, "me@example.com", "password");
		assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(UserService.NULL_USERNAME_MESSAGE);
		verify(userRepository, never()).checkUserExists(any(), any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanCreateUserFromInputThrowsIAEOnNullEmail() {
		final var input = new CreateUserInputDTO("monkey", null, "password");
		assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(UserService.NULL_EMAIL_MESSAGE);
		verify(userRepository, never()).checkUserExists(any(), any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanCreateUserFromInputThrowsIAEOnNullPassword() {
		final var input = new CreateUserInputDTO("monkey", "me@example.com", null);
		assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(UserService.NULL_PASSWORD_MESSAGE);
		verify(userRepository, never()).checkUserExists(any(), any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CannotCreateUserFromInputBecauseUserIsTaken() {
		final var input = new CreateUserInputDTO("username", "me@example.com", "password");

		// We're forcing the mock to return true so the code path gets executed.
		doReturn(true).when(userRepository).checkUserExists(any(), any());

		assertThatThrownBy(() -> userService.createUser(input, LocalDateTime.now()))
				.isInstanceOf(UserAlreadyExistsException.class)
				.hasMessage(UserAlreadyExistsException.USER_TAKEN_MESSAGE);
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanFilterUserFromRepositoryWithValidRetrieval() {
		final var user = users.get(0);
		doReturn(Optional.ofNullable(user)).when(userRepository).findById(1);

		final var retrieved = userService.getUserWithId(1);
		final var captor = ArgumentCaptor.forClass(Integer.class);
		verify(userRepository).findById(captor.capture());
		assertThat(captor.getValue()).isEqualTo(1);
		assertThat(retrieved).isEqualTo(user);
	}

	@Test
	void test_CannotFilterUserFromRepositoryWithInvalidRetrieval() {
		doReturn(Optional.ofNullable(null)).when(userRepository).findById(5);

		final var retrieved = userService.getUserWithId(5);
		final var captor = ArgumentCaptor.forClass(Integer.class);
		verify(userRepository).findById(captor.capture());
		verify(userRepository, never()).save(any());
		assertThat(captor.getValue()).isEqualTo(5);
		assertThat(retrieved).isNull();
	}

	@Test
	void test_CanUpdateUserFromRepositoryThrowsIAEWithNullDTO() {
		assertThatThrownBy(() -> userService.updateUserWithId(1, null)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(UserService.NULL_DTO_MESSAGE);
		verify(userRepository, never()).findById(any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanUpdateUserFromRepositoryThrowsIAEWithInvalidId() {
		doReturn(Optional.ofNullable(null)).when(userRepository).findById(5);

		final var userDTO = new CreateUserInputDTO("monkey", "me@example.com", "newPassword");
		final var captor = ArgumentCaptor.forClass(Integer.class);
		assertThatThrownBy(() -> userService.updateUserWithId(5, userDTO)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(UserService.INVALID_USER_ID + 5);
		verify(userRepository).findById(captor.capture());
		assertThat(captor.getValue()).isEqualTo(5);
		verify(userRepository, never()).save(any());
	}

	@Test
	void test_CanUpdateUserFromRepositoryWithValidId() {
		final var userDTO = new CreateUserInputDTO("monkey", "me@example.com", "newPassword");
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
	void test_CanDeleteUserFromRepository() {
		final var updatedUser = users.get(0);
		final var deletedTime = LocalDateTime.now();
		updatedUser.setDeleted(deletedTime);
		doAnswer(invocation -> {
			final var arg1 = invocation.getArgument(0, Integer.class);
			users.remove(0);
			return null;
		}).when(userRepository).deleteById(1);

		userService.deleteUserWithId(1);
		assertThat(users.get(0).getId()).isEqualTo(2);
		verify(userRepository).deleteById(1);
	}

	@Test
	public void validIdFindUserTest() {
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.findUserById(userEntity.getId()).isPresent());
		assertEquals(Optional.of(userInfoDto).get().getUserId(), userService.findUserById(userEntity.getId()).get().getUserId());
	}

	@Test
	public void validIdUpdateUserTest() {
		//update name, email, and password
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.updateUserProfile(userEntity.getId(), userProfileDto).isPresent());
		assertEquals(Optional.of(userProfileDto), userService.updateUserProfile(userEntity.getId(), userProfileDto));
		
		//update name only
		UserProfileDto userDtoNameOnly = UserProfileDto.builder().displayName("Test").build();
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoNameOnly).isPresent());
		assertEquals(Optional.of(userDtoNameOnly), userService.updateUserProfile(userEntity.getId(), userDtoNameOnly));
		
		//update email only
		UserProfileDto userDtoEmailOnly = UserProfileDto.builder().email("test@ss.com").build();
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoEmailOnly).isPresent());
		assertEquals(Optional.of(userDtoEmailOnly), userService.updateUserProfile(userEntity.getId(), userDtoEmailOnly));
		
		//update password only
		UserProfileDto userDtoPassOnly = UserProfileDto.builder().password("1234").build();
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoPassOnly).isPresent());
		assertEquals(Optional.of(userDtoPassOnly), userService.updateUserProfile(userEntity.getId(), userDtoPassOnly));
	}
	
	@Test
	public void validIdDeleteUserTest() {
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(userService.deleteUserAccount(userEntity.getId()));
	}

	@Test
	public void nullIdFindUserTest() {
		assertTrue(userService.findUserById(null).isEmpty());
		assertTrue(userService.updateUserProfile(null, userProfileDto).isEmpty());
	}

	@Test
	public void dataAccessExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(DataAccessException.class));
		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
	}

	@Test
	public void noSuchElementExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(NoSuchElementException.class));
		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
	}

	@Test
	public void illegalArgumentExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(IllegalArgumentException.class));
		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
	}
}
