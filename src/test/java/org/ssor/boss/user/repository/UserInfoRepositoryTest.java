/**
 * 
 */
package org.ssor.boss.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.ssor.boss.user.entity.UserEntity;

/**
 * @author Christian Angeles
 *
 */
@DataJpaTest
@Sql("classpath:data.sql")
public class UserInfoRepositoryTest {

	@Autowired
	UserRepository userRepository;

	private static UserEntity userEntity;

	@BeforeAll
	public static void userInfoSetup() {
		userEntity = UserEntity.builder().id(1).displayName("Jane Smith").email("janesmith@ss.com").created(LocalDateTime.now())
				.deleted(null).confirmed(true).build();
	}

	@Test
	public void findByIdEqualTest() {
		assertEquals(userEntity, userRepository.findById(1).orElse(null));
	}

	@Test
	public void findByIdNotEqualTest() {
		assertNotEquals(userEntity, userRepository.findById(2).orElse(null));
	}

	@Test
	public void findByIdNullIdTest() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.findById(null));
	}
}
