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
public class UpdateUserServiceTest {

	@TestConfiguration
	public static class UserInfoServiceContextConfig {
		@Bean
		public UpdateUserService userInfoService() {
			return new UpdateUserService();
		}
	}

	@Autowired
	UpdateUserService updateUserService;

	@MockBean
	UserRepository userRepository;
}
