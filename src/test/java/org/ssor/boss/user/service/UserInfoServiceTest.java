/**
 * 
 */
package org.ssor.boss.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.repository.UserRepository;
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
	UserInfoService userInfoService;

	@MockBean
	UserRepository userInfoDao;

	private static UserEntity userEntity;
	private static UserInfoDto userInfoDto;

	@BeforeAll
	public static void setUserInfo() {
		userEntity = UserEntity.builder().id(1).displayName("Joe Smith").email("joesmith@ss.com")
				.created(LocalDateTime.now()).deleted(null).confirmed(true).build();

		userInfoDto = UserInfoDto.builder().userId(1).displayName("Joe Smith").email("joesmith@ss.com")
				.created(LocalDateTime.now()).build();
	}

	@Test
	public void findUserByIdEqualsTest() {
		when(userInfoDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
		assertEquals(userInfoDto, userInfoService.findUserById(userEntity.getId()));
	}

	@Test
	public void findUserByIdNotEqualsTest() {
		when(userInfoDao.findById(1)).thenReturn(Optional.of(new UserEntity()));
		assertNotEquals(userInfoDto, userInfoService.findUserById(1));
	}
}
