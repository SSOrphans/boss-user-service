package org.ssor.boss.user.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTests
{
  LocalDateTime currentTime;
  UserEntity userEntity;

  @AfterEach
  void teardown()
  {
    currentTime = LocalDateTime.now();
    userEntity = null;
  }

  @Test
  void test_CanConstructWithoutParameters()
  {
    userEntity = new UserEntity();
    assertNotNull(userEntity);
  }

  @Test
  void test_CanConstructWithAllParameters()
  {
    userEntity = new UserEntity(1, "username", "example@example.com", "password", currentTime, null, true);
    assertNotNull(userEntity);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    // Should have already been tested.
    userEntity = new UserEntity();

    // Set.
    userEntity.setId(1);
    userEntity.setDisplayName("username");
    userEntity.setEmail("example@example.com");
    userEntity.setPassword("password");
    userEntity.setCreated(currentTime);
    userEntity.setDeleted(null);
    userEntity.setConfirmed(true);

    // Get.
    assertEquals(1, userEntity.getId());
    assertEquals("username", userEntity.getDisplayName());
    assertEquals("example@example.com", userEntity.getEmail());
    assertEquals("password", userEntity.getPassword());
    assertEquals(currentTime, userEntity.getCreated());
    assertNull(userEntity.getDeleted());
    assertTrue(userEntity.getConfirmed());
  }
}
