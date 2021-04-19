/**
 * 
 */
package org.ssor.boss.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.dto.ForgotPassEmailDto;
import org.ssor.boss.user.dto.ForgotPassTokenDto;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.dto.UserProfileDto;
import org.ssor.boss.user.service.UserService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Sql(scripts = "classpath:data.sql")
public class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UserProfileDto> jsonUserProfileDto;

	@Autowired
	JacksonTester<UserInfoDto> jsonUserInfoDto;

	@Autowired
	JacksonTester<ForgotPassEmailDto> jsonForgotPassEmailDto;

	@Autowired
	JacksonTester<ForgotPassTokenDto> jsonForgotPassTokenDto;

	@Autowired
	UserService userService;

	private Optional<UserInfoDto> userInfoDto;
	private Optional<UserProfileDto> userProfileDto;

	@Test
	public void getOkTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/1")).andReturn().getResponse();
		userInfoDto = userService.findUserById(1);

		assertTrue(userInfoDto.isPresent());
		assertEquals(jsonUserInfoDto.write(userInfoDto.get()).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void getNotFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/2")).andReturn().getResponse();
		userInfoDto = userService.findUserById(2);

		assertTrue(userInfoDto.isEmpty());
		assertEquals("User does not exist.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void putOkTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(
						put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(jsonUserProfileDto
										.write(UserProfileDto.builder().displayName("Test Sample").build()).getJson()))
				.andReturn().getResponse();
		userProfileDto = userService.updateUserProfile(1, UserProfileDto.builder().displayName("Test Sample").build());

		assertTrue(userProfileDto.isPresent());
		assertEquals("User profile updated.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void putNotFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(
						put("/api/v1/users/2").contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(jsonUserProfileDto
										.write(UserProfileDto.builder().displayName("Test Sample").build()).getJson()))
				.andReturn().getResponse();
		userProfileDto = userService.updateUserProfile(2, UserProfileDto.builder().displayName("Test Sample").build());

		assertTrue(userProfileDto.isEmpty());
		assertEquals("User does not exist.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void methodNotAllowedTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(put("/api/v1/users")).andReturn().getResponse();

		assertEquals("Method not allowed.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), mockResponse.getStatus());
	}

	@Test
	public void getBadRequestTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/not_int")).andReturn().getResponse();

		assertEquals("Malformed request syntax.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void putBadRequestTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(
						put("/api/v1/users/not_int").contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(jsonUserProfileDto
										.write(UserProfileDto.builder().displayName("Test Sample").build()).getJson()))
				.andReturn().getResponse();

		assertEquals("Malformed request syntax.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void getUriNotFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/randomness")).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidNameInputTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(
						jsonUserProfileDto.write(UserProfileDto.builder().displayName("Sample").build()).getJson()))
				.andReturn().getResponse();

		assertEquals("Invalid name.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidEmailInputTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(
						jsonUserProfileDto.write(UserProfileDto.builder().email("sample@notvalid").build()).getJson()))
				.andReturn().getResponse();

		assertEquals("Invalid email.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidPasswordInputTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUserProfileDto.write(UserProfileDto.builder()
								.password("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846--").build())
								.getJson()))
				.andReturn().getResponse();

		assertEquals("Invalid password hash.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidPasswordLengthInputTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(
						jsonUserProfileDto.write(UserProfileDto.builder().password("1a2b3c4").build()).getJson()))
				.andReturn().getResponse();

		assertEquals("Invalid password hash length.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void putInvalidInputIntegerTypeTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(
				put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).requestAttr("password", 1111))
				.andReturn().getResponse();

		assertEquals("Invalid request body.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountOkTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/3")).andReturn().getResponse();

		assertEquals("User account deleted.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void delUserAccountNotFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(delete("/api/v1/users/2")).andReturn().getResponse();

		assertEquals("User does not exist.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}

	@Test
	public void postForgotPassEmailTest() throws Exception {
		ForgotPassEmailDto forgotPassEmailDto = new ForgotPassEmailDto();
		forgotPassEmailDto.setEmail("janesmith@ss.com");
		MockHttpServletResponse mockResponse = mvc
				.perform(post("/api/v1/user/email").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassEmailDto.write(forgotPassEmailDto).getJson()))
				.andReturn().getResponse();

		assertEquals("Password reset link sent to email.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.ACCEPTED.value(), mockResponse.getStatus());
	}

	@Test
	public void putUpdateForgotPassTest() throws Exception {
		ForgotPassTokenDto forgotPassTokenDto = new ForgotPassTokenDto();
		forgotPassTokenDto.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW1wbGVAc3MuY29tIn0.nJS1ANuzIHmhmWBJZNZUhOlgbaLMt-tHc5WYUgHlUH10ihKw4EJNzrcjJ9WKfoN8hGfzDehSaWihG2Hy7nvngw");
		forgotPassTokenDto.setPassword("D74FF0EE8DA3B9806B18C877DBF29BBDE50B5BD8E4DAD7A3A725000FEB82E8F1");
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/user/password").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonForgotPassTokenDto.write(forgotPassTokenDto).getJson()))
				.andReturn().getResponse();

		assertEquals("User password was not updated.", mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}
}
