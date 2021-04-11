package org.ssor.boss.user.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ssor.boss.user.entity.UserEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTests
{
  @Autowired
  UserRepository userRepository;

  @Test
  void test_CheckUserExists()
  {
    final var user = new UserEntity(null, "monkey", "me@example.com", "password", LocalDateTime.now(), null, false);
    userRepository.save(user);

    final var expected = userRepository.checkUserExists(user.getDisplayName(), user.getEmail());
    assertThat(expected).isTrue();
  }
}
