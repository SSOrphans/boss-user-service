/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.service.UserInfoService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Sql("classpath:data.sql")
public class UserInfoControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<UserInfoDto> jsonUserInfoDto;

	@Autowired
	private UserInfoService userInfoService;

	private UserInfoDto userInfoDto;

	@BeforeEach
	public void setUserInfoDto() {
		userInfoDto = userInfoService.findUserById(1);
	}

	@Test
	public void getUserByIdOkTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/1")).andReturn().getResponse();
		assertEquals(jsonUserInfoDto.write(userInfoDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void getUserByIdBadRequestTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/should_be_integer")).andReturn()
				.getResponse();
		assertNotEquals(jsonUserInfoDto.write(userInfoDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void getUserByIdNotFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/random_path")).andReturn().getResponse();
		assertNotEquals(jsonUserInfoDto.write(userInfoDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}
}
