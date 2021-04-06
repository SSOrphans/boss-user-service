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
import org.ssor.boss.user.dto.UpdateUserDto;
import org.ssor.boss.user.service.UpdateUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest
@AutoConfigureJsonTesters
public class UpdateUserControllerUnitTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	JacksonTester<UpdateUserDto> jsonUpdateUserDto;

	@MockBean
	UpdateUserService updateUserService;

	private static UpdateUserDto updateUserDto;
}
