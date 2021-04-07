/**
 * 
 */
package org.ssor.boss.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.dto.UpdateUserDto;
import org.ssor.boss.user.service.UpdateUserService;

/**
 * @author Christian Angeles
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Sql("classpath:data.sql")
public class UpdateUserControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UpdateUserDto> jsonUpdateUserDto;

	@Autowired
	UpdateUserService updateUserService;

	private UpdateUserDto updateUserDto;

	@BeforeEach
	public void setUpdateUserDto() {
		updateUserDto = UpdateUserDto.builder().displayName("Sample").email("sample@ss.com").build();
	}

	@Test
	public void okTest() throws Exception {
		ResponseEntity<String> responseEntity = updateUserService.updateUserProfile(1, updateUserDto);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getBody(), mockResponse.getContentAsString());
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void noUserExistTest() throws Exception {
		ResponseEntity<String> responseEntity = updateUserService.updateUserProfile(200, updateUserDto);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/200").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getBody(), mockResponse.getContentAsString());
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
	}

	@Test
	public void badRequestTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/should_be_integer").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}

	@Test
	public void notFoundTest() throws Exception {
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/randomness").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
	}
}
