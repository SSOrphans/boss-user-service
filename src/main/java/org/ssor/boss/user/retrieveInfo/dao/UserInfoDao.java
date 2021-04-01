/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;

/**
 * @author Christian Angeles
 *
 */
@Repository
public interface UserInfoDao extends JpaRepository<UserInfo, Integer> {

}
