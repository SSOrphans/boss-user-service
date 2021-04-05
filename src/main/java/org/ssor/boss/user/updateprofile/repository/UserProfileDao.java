/**
 * 
 */
package org.ssor.boss.user.updateprofile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssor.boss.user.updateprofile.entity.UserProfile;

/**
 * @author Christian Angeles
 *
 */
@Repository
public interface UserProfileDao extends JpaRepository<UserProfile, Integer>{

}
