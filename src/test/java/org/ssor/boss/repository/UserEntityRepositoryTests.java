package org.ssor.boss.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.entity.UserType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserEntityRepositoryTests
{
  @Autowired
  UserEntityRepository userEntityRepository;

  @BeforeEach
  void setup()
  {
    final var user = new UserEntity(null, UserType.DEFAULT.ordinal(), 1, "monkey", "me@example.com", "password",
                                    LocalDateTime.now(), null, false);
    userEntityRepository.save(user);
  }

  @AfterEach
  void teardown()
  {
    userEntityRepository.deleteAll();
  }

  @Test
  void test_CheckUserExistsWithUsernameAndEmail()
  {
    final var expected = userEntityRepository.checkUserExistsWithUsernameAndEmail("monkey", "me@example.com");
    assertThat(expected).isTrue();
  }

  @Test
  void test_CheckUserExistsWithId()
  {
    final var expected = userEntityRepository.checkUserExistsWithId(1);
    assertThat(expected).isTrue();
  }

  @Test
  void test_CheckUserIsConfirmed()
  {
    final var expected = userEntityRepository.checkUserIsConfirmed(1);
    assertThat(expected).isFalse();
  }
}
