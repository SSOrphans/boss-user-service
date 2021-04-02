/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;

/**
 * @author Christian Angeles
 *
 */
@DataJpaTest
public class UserInfoDaoTest {
	
	@Autowired
	UserInfoDao userInfoDao;
	
	@Test
	public void findByIdTest() {
		UserInfo userInfoExpect = UserInfo.builder()
				.userId(1)
				.displayName("Jane Smith")
				.email("janesmith@ss.com")
				.created(new Timestamp(2077-01-01))
				.deleted(null)
				.confirmed(true)
				.locked(false)
				.build();
		
		;
        assertThat(userInfoDao.findById(1).equals(Optional.of(userInfoExpect)));
	}
}
