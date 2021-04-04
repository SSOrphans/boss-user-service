/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.retrieveInfo.service.UserInfoService;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest
@AutoConfigureJsonTesters
public class ControllerExceptionHandlerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<ErrorMessage> jsonErrorMessage;

	@MockBean
	private UserInfoService userInfoService;

	@Test
	public void typeMismatchExceptionTest() throws Exception {
		when(userInfoService.findUserById(1)).thenReturn(null);
		MockHttpServletResponse mockResponse = mvc.perform(get("/api/v1/users/should_be_integer")).andReturn()
				.getResponse();
		assertEquals(jsonErrorMessage.write(
				ErrorMessage.builder().status("400").error("Bad Request").message("Malformed request syntax").build())
				.getJson(), mockResponse.getContentAsString());
	}
}
