/**
 * 
 */
package org.ssor.boss.user.controller;

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
import org.ssor.boss.user.dto.RetrieveUserDto;
import org.ssor.boss.user.service.RetrieveUserService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Sql("classpath:data.sql")
public class RetrieveUserControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<RetrieveUserDto> jsonRetrieveUserDto;

	@Autowired
	RetrieveUserService userInfoService;

	private RetrieveUserDto retrieveUserDto;

	@BeforeEach
	public void setUserInfoDto() {
		retrieveUserDto = userInfoService.findUserById(1);
	}

	@Test
	public void okTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/1")).andReturn().getResponse();
		assertEquals(jsonRetrieveUserDto.write(retrieveUserDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void badRequestTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/should_be_integer")).andReturn()
				.getResponse();
		assertNotEquals(jsonRetrieveUserDto.write(retrieveUserDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void notFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc.perform(get("/random_path")).andReturn().getResponse();
		assertNotEquals(jsonRetrieveUserDto.write(retrieveUserDto).getJson(), mockResponse.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}
}
