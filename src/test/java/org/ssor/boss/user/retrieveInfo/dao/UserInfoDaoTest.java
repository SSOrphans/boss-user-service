/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;

/**
 * @author Christian Angeles
 *
 */
@DataJpaTest
@Sql("classpath:data.sql")
public class UserInfoDaoTest {

	@Autowired
	UserInfoDao userInfoDao;

	private static UserInfo userInfo;

	@BeforeAll
	public static void userInfoSetup() {
		userInfo = UserInfo.builder().userId(1).displayName("Jane Smith").email("janesmith@ss.com").created(null)
				.deleted(null).confirmed(true).locked(false).build();
	}

	@Test
	public void findByIdEqualTest() {
		assertEquals(userInfo, userInfoDao.findById(1).orElse(null));
	}

	@Test
	public void findByIdNotEqualTest() {
		assertNotEquals(userInfo, userInfoDao.findById(2).orElse(null));
	}

	@Test
	public void findByIdNullIdTest() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> userInfoDao.findById(null));
	}
}
