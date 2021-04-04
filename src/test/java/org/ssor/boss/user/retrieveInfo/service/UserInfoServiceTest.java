/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ssor.boss.user.retrieveInfo.dao.UserInfoDao;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Christian Angeles
 *
 */
@ExtendWith(SpringExtension.class)
public class UserInfoServiceTest {

	@TestConfiguration
	public static class UserInfoServiceContextConfig {
		@Bean
		public UserInfoService userInfoService() {
			return new UserInfoService();
		}
	}

	@Autowired
	private UserInfoService userInfoService;

	@MockBean
	private UserInfoDao userInfoDao;

	private static UserInfo userInfo;
	private static UserInfoDto userInfoDto;

	@BeforeAll
	public static void setUserInfo() {
		userInfo = UserInfo.builder().userId(1).displayName("Joe Smith").email("joesmith@ss.com")
				.created(new Timestamp(2077 - 01 - 01)).deleted(null).confirmed(true).locked(false).build();

		userInfoDto = UserInfoDto.builder().userId(1).displayName("Joe Smith").email("joesmith@ss.com")
				.created(new Timestamp(2077 - 01 - 01)).build();
	}

	@Test
	public void findUserByIdEqualsTest() {
		when(userInfoDao.findById(userInfo.getUserId())).thenReturn(Optional.of(userInfo));
		assertEquals(userInfoDto, userInfoService.findUserById(userInfo.getUserId()));
	}

	@Test
	public void findUserByIdNotEqualsTest() {
		when(userInfoDao.findById(1)).thenReturn(Optional.of(new UserInfo()));
		assertNotEquals(userInfoDto, userInfoService.findUserById(1));
	}
}
