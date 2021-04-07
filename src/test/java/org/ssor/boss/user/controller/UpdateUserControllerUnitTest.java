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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.dto.UpdateUserDto;
import org.ssor.boss.user.service.UpdateUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest(UpdateUserController.class)
@AutoConfigureJsonTesters
public class UpdateUserControllerUnitTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UpdateUserDto> jsonUpdateUserDto;

	@MockBean
	UpdateUserService updateUserService;

	private static UpdateUserDto updateUserDto;

	@BeforeAll
	public static void setUserInfoDto() {
		updateUserDto = UpdateUserDto.builder().displayName("Joe Smith").email("joesmith@ss.com").build();
	}

	@Test
	public void okTest() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body("Updated user profile.");
		when(updateUserService.updateUserProfile(1, updateUserDto)).thenReturn(responseEntity);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
	}

	@Test
	public void noUserExistTest() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body("User does not exist.");
		when(updateUserService.updateUserProfile(100, updateUserDto)).thenReturn(responseEntity);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/100").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
	}

	@Test
	public void badRequestTest() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Malformed request syntax.");
		when(updateUserService.updateUserProfile(1, updateUserDto)).thenReturn(responseEntity);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/api/v1/users/should_be_integer").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
	}

	@Test
	public void notFoundTest() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found.");
		when(updateUserService.updateUserProfile(1, updateUserDto)).thenReturn(responseEntity);
		MockHttpServletResponse mockResponse = mvc
				.perform(put("/randomness").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonUpdateUserDto.write(updateUserDto).getJson()))
				.andReturn().getResponse();
		assertEquals(responseEntity.getStatusCodeValue(), mockResponse.getStatus());
	}
}
