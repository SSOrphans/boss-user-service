/**
 * 
 */
package org.ssor.boss.user.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.service.UserInfoService;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest
public class ControllerExceptionHandlerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	UserInfoService userInfoService;

	@Test
	public void typeMismatchExceptionTest() throws Exception {
		when(userInfoService.findUserById(1)).thenReturn(null);
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/should_be_integer")).andReturn()
				.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
	}
}
