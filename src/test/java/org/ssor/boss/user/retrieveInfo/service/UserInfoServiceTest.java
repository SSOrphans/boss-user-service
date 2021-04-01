/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ssor.boss.user.retrieveInfo.dao.UserInfoDao;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;

/**
 * @author Christian Angeles
 *
 */
@WebMvcTest(UserInfoService.class)
public class UserInfoServiceTest {
	
	@Autowired
	private UserInfoService userInfoService;

    @MockBean
    private UserInfoDao userInfoDao;
    
    private UserInfo userInfoActual;
    private UserInfoDto userInfoExpect;
    
    @BeforeEach
    public void initUserInfo() {
    	userInfoActual = new UserInfo();
        userInfoActual.setUserId(1);
        userInfoActual.setDisplayName("Joe Smith");
        userInfoActual.setEmail("joesmith@ss.com");
        userInfoActual.setCreated(new Timestamp(2077-01-01));
        userInfoActual.setDeleted(null);
        userInfoActual.setConfirmed(true);
        userInfoActual.setLocked(false);

    	userInfoExpect = new UserInfoDto();
        userInfoExpect.setUserId(1);
        userInfoExpect.setDisplayName("Joe Smith");
        userInfoExpect.setEmail("joesmith@ss.com");
        userInfoExpect.setCreated(new Timestamp(2077-01-01));
        userInfoExpect.setDeleted(null);
        userInfoExpect.setConfirmed(true);
        userInfoExpect.setLocked(false);
    }
    
    @Test
    public void findUserByIdTest() {
        given(userInfoDao.findById(1)).willReturn(Optional.of(userInfoActual));
        assertThat(userInfoService.findUserById(1).equals(userInfoExpect));
    }
}
