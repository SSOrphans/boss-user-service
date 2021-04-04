/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.service.UserInfoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest
@AutoConfigureJsonTesters
public class UserInfoControllerUnitTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<UserInfoDto> jsonUserInfoDto;

	@MockBean
	private UserInfoService userInfoService;

	private static UserInfoDto userInfoDto;

	@BeforeAll
	public static void setUserInfoDto() {
		userInfoDto = UserInfoDto.builder().userId(1).displayName("Joe Smith").email("joesmith@ss.com").created(null)
				.build();
	}

	@Test
	public void getUserByIdOkTest() throws Exception {
		when(userInfoService.findUserById(userInfoDto.getUserId())).thenReturn(userInfoDto);
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
