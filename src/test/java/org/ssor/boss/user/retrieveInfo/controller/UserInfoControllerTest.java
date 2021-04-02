/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.controller;

import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * @author Christian Angeles
 *
 */
@AutoConfigureJsonTesters
@WebMvcTest(UserInfoController.class)
public class UserInfoControllerTest {
	
	@Autowired
	private MockMvc mvc;

    @MockBean
    private UserInfoService userInfoService;
    
    @Autowired
    private JacksonTester<UserInfoDto> jsonUserInfoDto;
    
    private UserInfoDto userInfoDtoActual, userInfoDtoExpect;
    
    @BeforeEach
    public void initUserInfoDto() {
    	userInfoDtoActual = UserInfoDto.builder()
				.userId(1)
				.displayName("Joe Smith")
				.email("joesmith@ss.com")
				.created(null)
				.deleted(null)
				.confirmed(true)
				.locked(false)
				.build();
        
    	userInfoDtoExpect = UserInfoDto.builder()
				.userId(1)
				.displayName("Joe Smith")
				.email("joesmith@ss.com")
				.created(null)
				.deleted(null)
				.confirmed(true)
				.locked(false)
				.build();
    }
    
    @Test
    public void getUserByIdJsonTest() throws Exception {
    	when(userInfoService.findUserById(1)).thenReturn(userInfoDtoActual);
    	MockHttpServletResponse jsonResponse = mvc.perform(get("/api/v1/users/1")).andReturn().getResponse();
    	
        assertThat(jsonResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonResponse.getContentAsString()).isEqualTo(jsonUserInfoDto.write(userInfoDtoExpect).getJson());
    }
}
