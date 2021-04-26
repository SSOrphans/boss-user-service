package org.ssor.boss.user.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTests
{
  LocalDateTime currentTime;
  UserEntity userEntity1;
  UserEntity userEntity2;
  UserEntity userEntity3;

  @AfterEach
  void teardown()
  {
    currentTime = LocalDateTime.now();
    userEntity1 = null;
    userEntity2 = null;
    userEntity3 = null;
  }

  @Test
  void test_CanConstructWithoutParameters()
  {
    userEntity1 = new UserEntity();
    assertThat(userEntity1).isNotNull();
  }

  @Test
  void test_CanConstructWithAllParameters()
  {
    userEntity1 = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    assertThat(userEntity1).isNotNull();
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    // Should have already been tested.
    userEntity1 = new UserEntity();

    // Set.
    userEntity1.setId(1);
    userEntity1.setDisplayName("username");
    userEntity1.setEmail("example@example.com");
    userEntity1.setPassword("password");
    userEntity1.setCreated(currentTime);
    userEntity1.setDeleted(null);
    userEntity1.setConfirmed(true);

    // Get.
    assertThat(userEntity1.getId()).isEqualTo(1);
    assertThat(userEntity1.getDisplayName()).isEqualTo("username");
    assertThat(userEntity1.getEmail()).isEqualTo("example@example.com");
    assertThat(userEntity1.getPassword()).isEqualTo("password");
    assertThat(userEntity1.getCreated()).isEqualTo(currentTime);
    assertThat(userEntity1.getDeleted()).isNull();
    assertThat(userEntity1.getConfirmed()).isTrue();
  }

  @Test
  void test_CanCompareWithEquals()
  {
    userEntity1 = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    userEntity2 = new UserEntity(2, "username", "example@example.com", "password", currentTime, null, true);
    userEntity3 = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    assertThat(userEntity1).isNotEqualTo(userEntity2);
    assertThat(userEntity1).isEqualTo(userEntity3);
  }

  @Test
  void test_CanCompareWithHashCode()
  {
    userEntity1 = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    userEntity2 = new UserEntity(2, "username", "example@example.com", "password", currentTime, null, true);
    userEntity3 = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    assertThat(userEntity1.hashCode()).isNotEqualTo(userEntity2.hashCode());
    assertThat(userEntity1.hashCode()).isEqualTo(userEntity3.hashCode());
  }
}
