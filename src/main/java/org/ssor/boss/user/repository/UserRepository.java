package org.ssor.boss.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ssor.boss.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	@Query("SELECT CASE WHEN COUNT(ue) > 0 " + "THEN TRUE ELSE FALSE END " + "FROM UserEntity ue "
			+ "WHERE ue.displayName = ?1 OR ue.email = ?2")
	boolean checkUserExists(String displayName, String email);

	@Query("SELECT CASE WHEN COUNT(ue) > 0 " + "THEN TRUE ELSE FALSE END " + "FROM UserEntity ue "
			+ "WHERE ue.email = ?1")
	boolean checkUserEmail(String email);
	
	Optional<UserEntity> findUserByEmail(String email);
}
