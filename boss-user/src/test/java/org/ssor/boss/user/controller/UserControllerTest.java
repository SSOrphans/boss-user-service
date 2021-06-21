/**
 *
 */
package org.ssor.boss.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.core.service.AwsSesService;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.*;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UpdateProfileInput;
import org.ssor.boss.user.service.ControllerService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser(username="gta5",password = "gtaV!@34",authorities = {"USER_DEFAULT","USER_VENDOR"})
public class UserControllerTest
{

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UpdateProfileInput> jsonUpdateProfileInput;

	@Autowired
	JacksonTester<UpdateUserInput> jsonUpdateUserInput;

	@Autowired
	JacksonTester<ForgotPassEmailInput> jsonForgotPassEmailInput;

	@Autowired
	JacksonTester<ForgotPassTokenInput> jsonForgotPassTokenInput;

	@Autowired
	JacksonTester<RegisterUserInput> jsonRegisterUserInput;

	@MockBean
	UserService userService;

	@MockBean
	AwsSesService awsSesService;

	@MockBean
	ControllerService controllerService;

	private UpdateProfileInput updateProfileInput;
	private RegisterUserOutput registerUserOutput;
	private UpdateUserInput updateUserInput;
	private SecureUserDetails secureUserDetails;

	@BeforeEach
	public void setup()
	{
		updateProfileInput = UpdateProfileInput.builder().email("sample@ss.com").fullName("test").address("address")
				.city("city").state("state").zip(12345).phone("18001235678").build();
		updateUserInput = UpdateUserInput.builder().username("gta5").password("gtaV!@34").email("gta5@ss.com").build();
		secureUserDetails = new SecureUserDetails();
		secureUserDetails.setUsername("user1");
	}

	@Test
	public void userInfoOkTest() throws Exception
	{
		when(userService.getSecureUserDetailsWithId(Mockito.anyInt())).thenReturn(Optional.ofNullable(secureUserDetails));
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/1").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void userInfoNotFoundTest() throws Exception
	{
		when(userService.getSecureUserDetailsWithId(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/2").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void updateUserProfileOkTest() throws Exception
	{
		when(userService.getSecureUserDetailsWithId(Mockito.anyInt())).thenReturn(Optional.ofNullable(secureUserDetails));
		when(userService.updateUserProfile(any(UpdateUserInput.class))).thenReturn("User profile updated");
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON)
						.content(jsonUpdateUserInput.write(updateUserInput).getJson()).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void updateUserProfileNotFoundTest() throws Exception
	{
		when(userService.getSecureUserDetailsWithId(Mockito.anyInt())).thenReturn(Optional.empty());
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/2").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserInput.write(updateUserInput).getJson()).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void methodNotAllowedTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(put("/api/v1/users").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), mockResponse.getStatus());
	}

	@Test
	public void getBadRequestTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/not_int").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void getUriNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/randomness").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidInputIntegerTypeTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).requestAttr("password", 1111).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountOkTest() throws Exception
	{
		when(userService.getSecureUserDetailsWithId(Mockito.anyInt())).thenReturn(Optional.ofNullable(secureUserDetails));
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/5").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/999999").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void getAllUserTest() throws Exception
	{
		when(userService.getAllUsersSecure()).thenReturn(new ArrayList<SecureUserDetails>());
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users").with(csrf())).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void addNewUserTest() throws Exception
	{
		RegisterUserInput registerUserInput = RegisterUserInput.builder().username("user1").email("user1@ss.com")
				.password("USer!@34").build();
		RegisterUserOutput registerUserOutput = RegisterUserOutput.builder().username("user1").email("user1@ss.com").build();
		when(userService.registerNewUser(any(RegisterUserInput.class), any(Instant.class))).thenReturn(registerUserOutput);
		MockHttpServletResponse mockResponse = mvc
				.perform(post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonRegisterUserInput.write(registerUserInput).getJson()).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.CREATED.value(), mockResponse.getStatus());
	}

	@Test
	public void forgotPassEmailTest() throws Exception
	{
		ForgotPassEmailInput forgotPassEmailInput = new ForgotPassEmailInput();
		forgotPassEmailInput.setEmail("gta5@ss.com");
		Email email = new Email("testSender@ss.com",forgotPassEmailInput.getEmail(),"Test subject","Test body");
		when(controllerService.sendPasswordReset(any(ForgotPassEmailInput.class))).thenReturn(Optional.of(email));
		when(awsSesService.sendEmail(any(Email.class))).thenReturn(new SendEmailResult());
		MockHttpServletResponse mockResponse = mvc
				.perform(post("/api/v1/users/forgot-password").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassEmailInput.write(forgotPassEmailInput).getJson()).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.ACCEPTED.value(), mockResponse.getStatus());
	}

	@Test
	public void updateForgotPassTest() throws Exception
	{
		ForgotPassTokenInput forgotPassTokenInput = new ForgotPassTokenInput();
		forgotPassTokenInput.setToken(
				"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMUBzcy5jb20ifQ.xLWpq08dGG4rr_daYoczPQSWWMH-6QDbGHKLK7bKFoVSJap_j2EjsPn71N4VyllMQX4OAumW9z0RbCu7b2XBOA");
		forgotPassTokenInput.setPassword("TEst!@34");
		when(controllerService.updateForgotPassword(any(ForgotPassTokenInput.class))).thenReturn(true);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/reset-password").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassTokenInput.write(forgotPassTokenInput).getJson()).with(csrf()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}
}
