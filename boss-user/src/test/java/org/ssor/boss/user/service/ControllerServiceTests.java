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
import org.ssor.boss.core.entity.AccountHolder;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.exception.ForgotPassTokenException;
import org.ssor.boss.core.exception.UserDataAccessException;
import org.ssor.boss.core.repository.AccountHolderRepository;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ControllerServiceTests
{

	@MockBean
	UserRepository userRepository;

	@MockBean
	AccountHolderRepository accountHolderRepository;

	@MockBean
	JwtForgotPassToken jwtForgotPassToken;

	@TestConfiguration
	public static class UserServiceTestContextConfig
	{
		@Bean
		public ControllerService controllerService()
		{
			return new ControllerService();
		}
	}

	@Autowired
	ControllerService controllerService;

	private User userEntity;
	private AccountHolder userAccount;
	private UserInfoOutput userInfoOutput;
	private UpdateUserInput updateUserInput;
	private ForgotPassEmailInput forgotPassEmailInput;
	private ForgotPassTokenInput forgotPassTokenInput;

	@BeforeEach
	void setup()
	{
		long time = Instant.now().toEpochMilli();
		LocalDate date = LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		userEntity = User.builder().id(1).username("test").email("test@ss.com").password("TEst!@34").created(time)
				.deleted(null).build();
		userAccount = AccountHolder.builder().fullName("Test Sample").dob(date).address("address").city("city")
				.state("state").zip(12345).phone("1234567").build();
		userInfoOutput = UserInfoOutput.builder().username("test").email("test@ss.com").created(date)
				.fullName("Test Sample").dob(date).address("address").city("city").state("state").zip(12345).phone("1234567")
				.build();
		updateUserInput = UpdateUserInput.builder().username("test1").email("test1@ss.com").password("TEst!@34").build();
		forgotPassEmailInput = new ForgotPassEmailInput();
		forgotPassTokenInput = new ForgotPassTokenInput();
	}

	@Test
	public void dataAccessExceptionTest()
	{
		when(userRepository.findById(null)).thenThrow(Mockito.mock(DataAccessException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void noSuchElementExceptionTest()
	{
		when(userRepository.findById(null)).thenThrow(Mockito.mock(NoSuchElementException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void illegalArgumentExceptionTest()
	{
		when(userRepository.findById(null)).thenThrow(Mockito.mock(IllegalArgumentException.class));
		assertThrows(UserDataAccessException.class, () -> controllerService.getUserInfo(null));
		assertThrows(UserDataAccessException.class, () -> controllerService.updateUserProfile(null, null));
		assertThrows(UserDataAccessException.class, () -> controllerService.deleteUserAccount(null));
	}

	@Test
	public void tokenValidationSignatureException()
	{
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(SignatureException.class));
		assertThrows(ForgotPassTokenException.class, () -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationMalformedJwtException()
	{
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(MalformedJwtException.class));
		assertThrows(ForgotPassTokenException.class, () -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationExpiredJwtException()
	{
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(ExpiredJwtException.class));
		assertThrows(ForgotPassTokenException.class, () -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationUnsupportedJwtException()
	{
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(UnsupportedJwtException.class));
		assertThrows(ForgotPassTokenException.class, () -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void tokenValidationIllegalArgumentException()
	{
		when(jwtForgotPassToken.validate(forgotPassTokenInput.getToken()))
				.thenThrow(Mockito.mock(IllegalArgumentException.class));
		assertThrows(ForgotPassTokenException.class, () -> controllerService.updateForgotPassword(forgotPassTokenInput));
	}

	@Test
	public void findUserTest()
	{
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
		when(accountHolderRepository.findById(userEntity.getId())).thenReturn(Optional.of(userAccount));

		assertTrue(controllerService.getUserInfo(userEntity.getId()).isPresent());
		
		assertEquals(Optional.of(userInfoOutput).get().getUsername(),
				controllerService.getUserInfo(userEntity.getId()).get().getUsername());

		assertEquals(Optional.of(userInfoOutput).get().getEmail(),
				controllerService.getUserInfo(userEntity.getId()).get().getEmail());
		
		assertEquals(Optional.of(userInfoOutput).get().getCreated(),
				controllerService.getUserInfo(userEntity.getId()).get().getCreated());
		
		assertEquals(Optional.of(userInfoOutput).get().getFullName(),
				controllerService.getUserInfo(userEntity.getId()).get().getFullName());
		
		assertEquals(Optional.of(userInfoOutput).get().getDob(),
				controllerService.getUserInfo(userEntity.getId()).get().getDob());
		
		assertEquals(Optional.of(userInfoOutput).get().getAddress(),
				controllerService.getUserInfo(userEntity.getId()).get().getAddress());
		
		assertEquals(Optional.of(userInfoOutput).get().getCity(),
				controllerService.getUserInfo(userEntity.getId()).get().getCity());
		
		assertEquals(Optional.of(userInfoOutput).get().getState(),
				controllerService.getUserInfo(userEntity.getId()).get().getState());
		
		assertEquals(Optional.of(userInfoOutput).get().getZip(),
				controllerService.getUserInfo(userEntity.getId()).get().getZip());
		
		assertEquals(Optional.of(userInfoOutput).get().getPhone(),
				controllerService.getUserInfo(userEntity.getId()).get().getPhone());
	}

	@Test
	public void updateUserTest()
	{
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

		assertTrue(controllerService.updateUserProfile(userEntity.getId(), updateUserInput));
	}

	@Test
	public void deleteUserTest()
	{
		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
		assertTrue(controllerService.deleteUserAccount(userEntity.getId()));

		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());
		assertFalse(controllerService.deleteUserAccount(userEntity.getId()));
	}

	@Test
	public void nullIdFindUserTest()
	{
		assertTrue(controllerService.getUserInfo(null).isEmpty());
	}

	@Test
	public void sendPasswordResetTest()
	{
		forgotPassEmailInput.setEmail("test@ss.com");
		when(jwtForgotPassToken.generateAccessToken(forgotPassEmailInput.getEmail())).thenReturn("someValidToken");

		when(userRepository.existsUserByEmail(forgotPassEmailInput.getEmail())).thenReturn(true);
		assertTrue(controllerService.sendPasswordReset(forgotPassEmailInput));

		when(userRepository.existsUserByEmail(forgotPassEmailInput.getEmail())).thenReturn(false);
		assertFalse(controllerService.sendPasswordReset(forgotPassEmailInput));
	}

	@Test
	public void updateForgotPasswordTest()
	{
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
		userEntity.setDeleted(Instant.now().toEpochMilli());
		when(userRepository.getUserByEmail("test@ss.com")).thenReturn(Optional.of(userEntity));
		assertFalse(controllerService.updateForgotPassword(forgotPassTokenInput));
	}
}
