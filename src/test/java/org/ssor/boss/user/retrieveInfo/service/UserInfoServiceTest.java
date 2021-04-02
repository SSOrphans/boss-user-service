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
import org.junit.jupiter.api.extension.ExtendWith;
import org.ssor.boss.user.retrieveInfo.dao.UserInfoDao;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Christian Angeles
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {
	

    @Mock
    private UserInfoDao userInfoDao;
    
    @InjectMocks
	private UserInfoService userInfoService;
    
    private UserInfo userInfoActual;
    private UserInfoDto userInfoExpect;
    
    @BeforeEach
    public void initUserInfo() {
    	userInfoActual = UserInfo.builder()
				.userId(1)
				.displayName("Joe Smith")
				.email("joesmith@ss.com")
				.created(new Timestamp(2077-01-01))
				.deleted(null)
				.confirmed(true)
				.locked(false)
				.build();

    	userInfoExpect = UserInfoDto.builder()
				.userId(1)
				.displayName("Joe Smith")
				.email("joesmith@ss.com")
				.created(new Timestamp(2077-01-01))
				.deleted(null)
				.confirmed(true)
				.locked(false)
				.build();
    }
    
    @Test
    public void findUserByIdTest() {
        given(userInfoDao.findById(1)).willReturn(Optional.of(userInfoActual));
        assertThat(userInfoService.findUserById(1).equals(userInfoExpect));
    }
}
