package org.ssor.boss.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.exception.ForgotPassTokenException;
import org.ssor.boss.core.exception.UserDataAccessException;
import org.ssor.boss.core.repository.UserRepository;
import org.ssor.boss.core.transfer.UpdateUserInput;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UserInfoOutput;
import org.ssor.boss.user.security.JwtForgotPassToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ControllerServiceTests {

	@MockBean
	UserRepository userRepository;

	@MockBean
	JwtForgotPassToken jwtForgotPassToken;

	@TestConfiguration
	public static class UserServiceTestContextConfig {
		@Bean
		public ControllerService controllerService() {
			return new ControllerService();
		}
	}

	@Autowired
	ControllerService controllerService;

	private User userEntity;
	private UserInfoOutput userInfoOutput;
	private UpdateUserInput updateUserInput;
	private ForgotPassEmailInput forgotPassEmailInput;
	private ForgotPassTokenInput forgotPassTokenInput;

	@BeforeEach
	void setup() {
		LocalDateTime time = LocalDateTime.now();
		userEntity = User.builder().id(1).username("User1").email("user1@ss.com").password("USer!@34").created(time)
				.deleted(null).build();
		userInfoOutput = UserInfoOutput.builder().username("User1").email("user1@ss.com").created(time).build();
		updateUserInput = UpdateUserInput.builder().username("User1").email("user1@ss.com").password("USer!@34").build();
		forgotPassEmailInput = new ForgotPassEmailInput();
		forgotPassTokenInput = new ForgotPassTokenInput();
	}

	@Test
	public void dataAccessExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(DataAccessException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void noSuchElementExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(NoSuchElementException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void illegalArgumentExceptionTest() {
		when(userRepository.findById(null)).thenThrow(Mockito.mock(IllegalArgumentException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void tokenValidationSignatureException() {
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(SignatureException.class));
		assertThrows(ForgotPassTokenException.class,
				() -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationMalformedJwtException() {
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(MalformedJwtException.class));
		assertThrows(ForgotPassTokenException.class,
				() -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationExpiredJwtException() {
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(ExpiredJwtException.class));
		assertThrows(ForgotPassTokenException.class,
				() -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationUnsupportedJwtException() {
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(UnsupportedJwtException.class));
		assertThrows(ForgotPassTokenException.class,
				() -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationIllegalArgumentException() {
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(IllegalArgumentException.class));
		assertThrows(ForgotPassTokenException.class,
				() -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void findUserTest() {
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(controllerService.getUserInfo(userEntity.getId()).isPresent());
		assertEquals(Optional.of(userInfoOutput).get().getUsername(),
				controllerService.getUserInfo(userEntity.getId()).get().getUsername());

		assertTrue(controllerService.getUserInfo(userEntity.getId()).isPresent());
		assertEquals(Optional.of(userInfoOutput).get().getEmail(),
				controllerService.getUserInfo(userEntity.getId()).get().getEmail());

		assertTrue(controllerService.getUserInfo(userEntity.getId()).isPresent());
		assertEquals(Optional.of(userInfoOutput).get().getCreated(),
				controllerService.getUserInfo(userEntity.getId()).get().getCreated());
	}

	@Test
	public void updateUserTest() {
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(controllerService.updateUserProfile(userEntity.getId(), updateUserInput));
	}

	@Test
	public void deleteUserTest() {
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
		assertTrue(controllerService.deleteUserAccount(userEntity.getId()));

		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());
		assertFalse(controllerService.deleteUserAccount(userEntity.getId()));
	}

	@Test
	public void nullIdFindUserTest() {
		assertTrue(controllerService.getUserInfo(null).isEmpty());
	}

	@Test
	public void sendPasswordResetTest() {
		forgotPassEmailInput.setEmail("test@ss.com");
		when(jwtForgotPassToken.generateAccessToken(forgotPassEmailInput.getEmail())).thenReturn("someValidToken");

		when(userRepository.existsUserByEmail(forgotPassEmailInput.getEmail())).thenReturn(true);
		assertTrue(controllerService.sendPasswordReset(forgotPassEmailInput));

		when(userRepository.existsUserByEmail(forgotPassEmailInput.getEmail())).thenReturn(false);
		assertFalse(controllerService.sendPasswordReset(forgotPassEmailInput));
	}

	@Test
	public void updateForgotPasswordTest() {
		// valid token, password, and entity
		forgotPassTokenInput.setToken("test@ss.com");
		forgotPassTokenInput.setPassword("someValidPassword");
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken())).thenReturn(true);
		when(jwtForgotPassToken.getUserEmail(forgotPassTokenInput.getToken())).thenReturn("test@ss.com");
		when(userRepository.getUserByEmail("test@ss.com")).thenReturn(Optional.of(userEntity));
		assertTrue(controllerService.updateForgotPassword(forgotPassTokenInput));

		// null password
		forgotPassTokenInput.setPassword(null);
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken())).thenReturn(true);
		assertTrue(controllerService.updateForgotPassword(forgotPassTokenInput));

		// empty password
		forgotPassTokenInput.setPassword("");
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken())).thenReturn(true);
		assertTrue(controllerService.updateForgotPassword(forgotPassTokenInput));

		// invalid token
		forgotPassTokenInput.setToken("test@ss.com");
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken())).thenReturn(false);
		assertFalse(controllerService.updateForgotPassword(forgotPassTokenInput));

		// empty entity
		when(userRepository.getUserByEmail("test@ss.com")).thenReturn(Optional.empty());
		assertFalse(controllerService.updateForgotPassword(forgotPassTokenInput));

		// entity "delete" not null
		userEntity.setDeleted(LocalDateTime.now());
		when(userRepository.getUserByEmail("test@ss.com")).thenReturn(Optional.of(userEntity));
		assertFalse(controllerService.updateForgotPassword(forgotPassTokenInput));
	}
}
