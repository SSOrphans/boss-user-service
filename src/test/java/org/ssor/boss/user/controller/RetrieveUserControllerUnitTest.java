/**
 * 
 */
package org.ssor.boss.user.controller;

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
import org.ssor.boss.user.dto.RetrieveUserDto;
import org.ssor.boss.user.service.RetrieveUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest(RetrieveUserController.class)
@AutoConfigureJsonTesters
public class RetrieveUserControllerUnitTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<RetrieveUserDto> jsonUserInfoDto;

	@MockBean
	RetrieveUserService userInfoService;

	private static RetrieveUserDto userInfoDto;

	@BeforeAll
	public static void setUserInfoDto() {
		userInfoDto = RetrieveUserDto.builder().userId(1).displayName("Joe Smith").email("joesmith@ss.com").created(null)
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
