package org.ssor.boss.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssor.boss.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>
{
}
