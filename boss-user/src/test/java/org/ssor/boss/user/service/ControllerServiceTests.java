//package org.ssor.boss.user.service;
//
//import org.assertj.core.util.Lists;
//import org.junit.Before;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.dao.DataAccessException;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.ssor.boss.core.entity.User;
//import org.ssor.boss.core.repository.UserRepository;
//import org.ssor.boss.user.dto.ForgotPassEmailDto;
//import org.ssor.boss.user.dto.ForgotPassTokenDto;
//import org.ssor.boss.user.dto.UserInfoDto;
//import org.ssor.boss.user.exception.ForgotPassTokenException;
//import org.ssor.boss.user.exception.UserDataAccessException;
//import org.ssor.boss.user.security.JwtForgotPassToken;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureException;
//import io.jsonwebtoken.UnsupportedJwtException;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//class ControllerServiceTests {
//
//	@MockBean
//	UserRepository userRepository;
//
//	@MockBean
//	JwtForgotPassToken jwtForgotPassToken;
//
//	@TestConfiguration
//	public static class UserServiceTestContextConfig {
//		@Bean
//		public ControllerService userService() {
//			return new ControllerService();
//		}
//	}
//
//	@Autowired
//	ControllerService userService;
//
//	private static User userEntity;
//	private static UserInfoDto userInfoDto;
//	private static UserProfileDto userProfileDto;
//	private static ForgotPassEmailDto forgotPassEmailDto;
//	private static ForgotPassTokenDto forgotPassTokenDto;
//
//	@Before
//	void setup() {
//		userEntity = User.builder().id(1).username("Test").email("test@ss.com").password("1234")
//				.created(LocalDateTime.now()).deleted(null).build();
//		userInfoDto = UserInfoDto.builder().userId(1).displayName("Test").email("test@ss.com")
//				.created(LocalDateTime.now()).build();
//		userProfileDto = UserProfileDto.builder().displayName("Test").email("test@ss.com").password("1234").build();
//		forgotPassEmailDto = new ForgotPassEmailDto();
//		forgotPassTokenDto = new ForgotPassTokenDto();
//	}
//
//	@Test
//	public void dataAccessExceptionTest() {
//		when(userRepository.findById(null)).thenThrow(Mockito.mock(DataAccessException.class));
//		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
//		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
//		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
//	}
//
//	@Test
//	public void noSuchElementExceptionTest() {
//		when(userRepository.findById(null)).thenThrow(Mockito.mock(NoSuchElementException.class));
//		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
//		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
//		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
//	}
//
//	@Test
//	public void illegalArgumentExceptionTest() {
//		when(userRepository.findById(null)).thenThrow(Mockito.mock(IllegalArgumentException.class));
//		assertThrows(UserDataAccessException.class, () -> userService.findUserById(null));
//		assertThrows(UserDataAccessException.class, () -> userService.updateUserProfile(null, null));
//		assertThrows(UserDataAccessException.class, () -> userService.deleteUserAccount(null));
//	}
//	
//	@Test
//	public void tokenValidationSignatureException() {
//		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenThrow(Mockito.mock(SignatureException.class));
//		assertThrows(ForgotPassTokenException.class, () -> userService.updateForgotPassword(forgotPassTokenDto));
//	}
//	
//	@Test
//	public void tokenValidationMalformedJwtException() {
//		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenThrow(Mockito.mock(MalformedJwtException.class));
//		assertThrows(ForgotPassTokenException.class, () -> userService.updateForgotPassword(forgotPassTokenDto));
//	}
//	
//	@Test
//	public void tokenValidationExpiredJwtException() {
//		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenThrow(Mockito.mock(ExpiredJwtException.class));
//		assertThrows(ForgotPassTokenException.class, () -> userService.updateForgotPassword(forgotPassTokenDto));
//	}
//	
//	@Test
//	public void tokenValidationUnsupportedJwtException() {
//		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenThrow(Mockito.mock(UnsupportedJwtException.class));
//		assertThrows(ForgotPassTokenException.class, () -> userService.updateForgotPassword(forgotPassTokenDto));
//	}
//	
//	@Test
//	public void tokenValidationIllegalArgumentException() {
//		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenThrow(Mockito.mock(IllegalArgumentException.class));
//		assertThrows(ForgotPassTokenException.class, () -> userService.updateForgotPassword(forgotPassTokenDto));
//	}
//
//	@Test
//	public void validIdFindUserTest() {
//		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
//
//		assertTrue(userService.findUserById(userEntity.getId()).isPresent());
//		assertEquals(Optional.of(userInfoDto).get().getUserId(),
//				userService.findUserById(userEntity.getId()).get().getUserId());
//	}
////
////	@Test
////	public void validIdUpdateUserTest() {
////		// update name, email, and password
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
////
////		assertTrue(userService.updateUserProfile(userEntity.getId());
////		assertEquals(Optional.of(userProfileDto), userService.updateUserProfile(userEntity.getId(), userProfileDto));
////
////		// update name only
////		UserProfileDto userDtoNameOnly = UserProfileDto.builder().displayName("Test").build();
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
////
////		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoNameOnly).isPresent());
////		assertEquals(Optional.of(userDtoNameOnly), userService.updateUserProfile(userEntity.getId(), userDtoNameOnly));
////
////		// update email only
////		UserProfileDto userDtoEmailOnly = UserProfileDto.builder().email("test@ss.com").build();
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
////
////		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoEmailOnly).isPresent());
////		assertEquals(Optional.of(userDtoEmailOnly),
////				userService.updateUserProfile(userEntity.getId(), userDtoEmailOnly));
////
////		// update password only
////		UserProfileDto userDtoPassOnly = UserProfileDto.builder().password("1234").build();
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
////
////		assertTrue(userService.updateUserProfile(userEntity.getId(), userDtoPassOnly).isPresent());
////		assertEquals(Optional.of(userDtoPassOnly), userService.updateUserProfile(userEntity.getId(), userDtoPassOnly));
////	}
////
////	@Test
////	public void deleteUserTest() {
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
////		assertTrue(userService.deleteUserAccount(userEntity.getId()));
////
////		when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());
////		assertFalse(userService.deleteUserAccount(userEntity.getId()));
////	}
////
////	@Test
////	public void nullIdFindUserTest() {
////		assertTrue(userService.findUserById(null).isEmpty());
////		assertTrue(userService.updateUserProfile(null, userProfileDto).isEmpty());
////	}
////
////	@Test
////	public void sendPasswordResetTest() {
////		forgotPassEmailDto.setEmail("test@ss.com");
////		when(jwtForgotPassToken.generateAccessToken(forgotPassEmailDto.getEmail())).thenReturn("someValidToken");
////
////		when(userRepository.checkUserEmail(forgotPassEmailDto.getEmail())).thenReturn(true);
////		assertTrue(userService.sendPasswordReset(forgotPassEmailDto));
////
////		when(userRepository.checkUserEmail(forgotPassEmailDto.getEmail())).thenReturn(false);
////		assertFalse(userService.sendPasswordReset(forgotPassEmailDto));
////	}
////
////	@Test
////	public void updateForgotPasswordTest() {
////		// valid token, password, and entity
////		forgotPassTokenDto.setToken("test@ss.com");
////		forgotPassTokenDto.setPassword("someValidPassword");
////		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenReturn(true);
////		when(jwtForgotPassToken.getUserEmail(forgotPassTokenDto.getToken())).thenReturn("test@ss.com");
////		when(userRepository.findUserByEmail("test@ss.com")).thenReturn(Optional.of(userEntity));
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.of(forgotPassTokenDto));
////
////		// null password
////		forgotPassTokenDto.setPassword(null);
////		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenReturn(true);
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.empty());
////
////		// empty password
////		forgotPassTokenDto.setPassword("");
////		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenReturn(true);
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.empty());
////
////		// invalid token
////		forgotPassTokenDto.setToken("test@ss.com");
////		when(jwtForgotPassToken.validate(forgotPassTokenDto.getToken())).thenReturn(false);
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.empty());
////
////		// empty entity
////		when(userRepository.findUserByEmail("test@ss.com")).thenReturn(Optional.empty());
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.empty());
////
////		// entity "delete" not null
////		userEntity.setDeleted(LocalDateTime.now());
////		when(userRepository.findUserByEmail("test@ss.com")).thenReturn(Optional.of(userEntity));
////		assertEquals(userService.updateForgotPassword(forgotPassTokenDto), Optional.empty());
////	}
//}
