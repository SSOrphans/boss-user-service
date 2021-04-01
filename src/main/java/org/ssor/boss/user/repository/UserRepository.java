package org.ssor.boss.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.ssor.boss.user.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>
{
}
