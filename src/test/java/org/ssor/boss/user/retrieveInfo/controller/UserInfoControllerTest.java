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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.service.UserInfoService;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
    	userInfoDtoActual = new UserInfoDto();
        userInfoDtoActual.setUserId(1);
        userInfoDtoActual.setDisplayName("Joe Smith");
        userInfoDtoActual.setEmail("joesmith@ss.com");
        userInfoDtoActual.setCreated(null);
        userInfoDtoActual.setDeleted(null);
        userInfoDtoActual.setConfirmed(true);
        userInfoDtoActual.setLocked(false);
        
    	userInfoDtoExpect = new UserInfoDto();
        userInfoDtoExpect.setUserId(1);
        userInfoDtoExpect.setDisplayName("Joe Smith");
        userInfoDtoExpect.setEmail("joesmith@ss.com");
        userInfoDtoExpect.setCreated(null);
        userInfoDtoExpect.setDeleted(null);
        userInfoDtoExpect.setConfirmed(true);
        userInfoDtoExpect.setLocked(false);
    }
    
    @Test
    public void getUserByIdJsonTest() throws Exception {
    	given(userInfoService.findUserById(1)).willReturn(userInfoDtoActual);
    	MockHttpServletResponse jsonResponse = mvc.perform(get("/api/v1/users/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
    	
        assertThat(jsonResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonResponse.getContentAsString()).isEqualTo(jsonUserInfoDto.write(userInfoDtoExpect).getJson());
    }
    
    @Test
    public void getUserByIdXmlTest() throws Exception {
    	given(userInfoService.findUserById(1)).willReturn(userInfoDtoActual);
    	MockHttpServletResponse xmlResponse = mvc.perform(get("/api/v1/users/1").accept(MediaType.APPLICATION_XML_VALUE)).andReturn().getResponse();
    	
        assertThat(xmlResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(xmlResponse.getContentAsString()).isEqualTo(new XmlMapper().writeValueAsString(userInfoDtoExpect));
    }
}
