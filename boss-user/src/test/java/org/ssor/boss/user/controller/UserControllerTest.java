/**
 *
 */
package org.ssor.boss.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.RegisterUserInput;
import org.ssor.boss.core.transfer.RegisterUserOutput;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UpdateProfileInput;
import org.ssor.boss.user.dto.UserInfoOutput;
import org.ssor.boss.user.service.ControllerService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest
{

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UpdateProfileInput> jsonUpdateProfileInput;

	@Autowired
	JacksonTester<ForgotPassEmailInput> jsonForgotPassEmailInput;

	@Autowired
	JacksonTester<ForgotPassTokenInput> jsonForgotPassTokenInput;

	@Autowired
	JacksonTester<RegisterUserInput> jsonRegisterUserInput;

	@MockBean
	UserService userService;

	@Autowired
	ControllerService controllerService;

	private UpdateProfileInput updateProfileInput;

	@BeforeEach
	public void setup()
	{
		updateProfileInput = UpdateProfileInput.builder().email("sample@ss.com").fullName("test").address("address")
				.city("city").state("state").zip(12345).phone("18001235678").build();
	}

	@Test
	public void userInfoOkTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/1")).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void userInfoNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/2")).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void updateUserProfileOkTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateProfileInput.write(updateProfileInput).getJson()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void updateUserProfileNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/2").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateProfileInput.write(updateProfileInput).getJson()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void methodNotAllowedTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(put("/api/v1/users")).andReturn().getResponse();

		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), mockResponse.getStatus());
	}

	@Test
	public void getBadRequestTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/not_int")).andReturn().getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void getUriNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(get("/randomness")).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidInputIntegerTypeTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).requestAttr("password", 1111))
				.andReturn().getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountOkTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/5")).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountNotFoundTest() throws Exception
	{
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/5")).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void getAllUserTest() throws Exception
	{
		when(userService.getAllUsersUnsecure()).thenReturn(new ArrayList<User>());
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users")).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void addNewUserTest() throws Exception
	{
		RegisterUserInput registerUserInput = RegisterUserInput.builder().username("user1").email("user1@ss.com")
				.password("USer!@34").build();
		when(userService.registerNewUser(registerUserInput, Instant.now())).thenReturn(new RegisterUserOutput());
		MockHttpServletResponse mockResponse = mvc
				.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonRegisterUserInput.write(registerUserInput).getJson()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.CREATED.value(), mockResponse.getStatus());
	}

	@Test
	public void forgotPassEmailTest() throws Exception
	{
		ForgotPassEmailInput forgotPassEmailInput = new ForgotPassEmailInput();
		forgotPassEmailInput.setEmail("test@ss.com");
		MockHttpServletResponse mockResponse = mvc
				.perform(post("/api/v1/user/email").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassEmailInput.write(forgotPassEmailInput).getJson()))
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
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/user/password").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassTokenInput.write(forgotPassTokenInput).getJson()))
				.andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}
}
