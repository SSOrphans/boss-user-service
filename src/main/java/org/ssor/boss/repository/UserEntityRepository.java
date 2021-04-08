package org.ssor.boss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ssor.boss.entity.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer>
{
  @Query("SELECT CASE WHEN COUNT(ue) > 0 " +
         "THEN TRUE ELSE FALSE END " +
         "FROM UserEntity ue " +
         "WHERE ue.displayName = ?1 OR ue.email = ?2")
  boolean checkUserExistsWithUsernameAndEmail(String displayName, String email);

  @Query("SELECT CASE WHEN COUNT(ue) > 0 " +
         "THEN TRUE ELSE FALSE END " +
         "FROM UserEntity ue " +
         "WHERE ue.id = ?1")
  boolean checkUserExistsWithId(final int id);

  @Query("SELECT CASE WHEN COUNT(ue) > 0 " +
         "THEN TRUE ELSE FALSE END " +
         "FROM UserEntity ue where ue.id = ?1 AND ue.confirmed = true")
  boolean checkUserIsConfirmed(final int id);
}
