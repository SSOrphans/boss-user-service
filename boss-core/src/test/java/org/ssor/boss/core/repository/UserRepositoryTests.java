package org.ssor.boss.core.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ssor.boss.core.entity.UserType;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests
{
  @Autowired
  UserRepository userRepository;

  @Test
  void test_CanGetUserByUsername()
  {
    final var users = userRepository.findAll();

    final var possibleUser = userRepository.getUserByUsername("SoraKatadzuma");
    assertThat(possibleUser.isPresent()).isTrue();

    final var user = possibleUser.get();
    assertThat(user.getId()).isEqualTo(1);
    assertThat(user.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(user.getBranchId()).isEqualTo(1);
    assertThat(user.getUsername()).isEqualTo("SoraKatadzuma");
    assertThat(user.getEmail()).isEqualTo("sorakatadzuma@gmail.com");
    assertThat(user.getPassword()).isEqualTo("D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E");
    assertThat(user.getCreated()).isNotNull();
    assertThat(user.getDeleted()).isNull();
    assertThat(user.isEnabled()).isTrue();
    assertThat(user.isLocked()).isFalse();
  }

  @Test
  void test_CanGetUserByEmail()
  {
    final var possibleUser = userRepository.getUserByEmail("me@example.com");
    assertThat(possibleUser.isPresent()).isTrue();

    final var user = possibleUser.get();
    assertThat(user.getId()).isEqualTo(2);
    assertThat(user.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(user.getBranchId()).isEqualTo(1);
    assertThat(user.getUsername()).isEqualTo("Monkey");
    assertThat(user.getEmail()).isEqualTo("me@example.com");
    assertThat(user.getPassword()).isEqualTo("D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E");
    assertThat(user.getCreated()).isNotNull();
    assertThat(user.getDeleted()).isNull();
    assertThat(user.isEnabled()).isTrue();
    assertThat(user.isLocked()).isFalse();
  }

  @Test
  void test_CanGetUserByUsernameOrEmail()
  {
    var possibleUser = userRepository.getUserByUsernameOrEmail("SoraKatadzuma", "SoraKatadzuma");
    assertThat(possibleUser.isPresent()).isTrue();

    var user = possibleUser.get();
    assertThat(user.getId()).isEqualTo(1);
    assertThat(user.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(user.getBranchId()).isEqualTo(1);
    assertThat(user.getUsername()).isEqualTo("SoraKatadzuma");
    assertThat(user.getEmail()).isEqualTo("sorakatadzuma@gmail.com");
    assertThat(user.getPassword()).isEqualTo("D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E");
    assertThat(user.getCreated()).isNotNull();
    assertThat(user.getDeleted()).isNull();
    assertThat(user.isEnabled()).isTrue();
    assertThat(user.isLocked()).isFalse();

    // Test again with email instead.
    possibleUser = userRepository.getUserByUsernameOrEmail("sorakatadzuma@gmail.com", "sorakatadzuma@gmail.com");
    assertThat(possibleUser.isPresent()).isTrue();

    user = possibleUser.get();
    assertThat(user.getId()).isEqualTo(1);
    assertThat(user.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(user.getBranchId()).isEqualTo(1);
    assertThat(user.getUsername()).isEqualTo("SoraKatadzuma");
    assertThat(user.getEmail()).isEqualTo("sorakatadzuma@gmail.com");
    assertThat(user.getPassword()).isEqualTo("D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E");
    assertThat(user.getCreated()).isNotNull();
    assertThat(user.getDeleted()).isNull();
    assertThat(user.isEnabled()).isTrue();
    assertThat(user.isLocked()).isFalse();
  }

  @Test
  void test_CanCheckUserExistsByUsername()
  {
    assertThat(userRepository.existsUserByUsername("Monkey")).isTrue();
  }

  @Test
  void test_CanCheckUserExistsByEmail()
  {
    assertThat(userRepository.existsUserByEmail("sorakatadzuma@gmail.com")).isTrue();
  }

  @Test
  void test_CanCheckUserExistsByUsernameOrEmail()
  {
    assertThat(userRepository.existsUserByUsernameOrEmail("SoraKatadzuma", null)).isTrue();
    assertThat(userRepository.existsUserByUsernameOrEmail(null, "me@example.com")).isTrue();
  }
}
