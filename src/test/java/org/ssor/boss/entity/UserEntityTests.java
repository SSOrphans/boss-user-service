package org.ssor.boss.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: Test builder.
class UserEntityTests
{
  static final Integer TYPE_ID = UserType.DEFAULT.ordinal();
  static final String USER_NAME = "username";
  static final String EMAIL = "me@example.com";
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";
  static final LocalDateTime CREATED = LocalDateTime.now();

  static UserEntity userEntity1;
  static UserEntity userEntity2;
  static UserEntity userEntity3;

  @AfterEach
  void teardown()
  {
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
    userEntity1 = new UserEntity(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true);
    assertThat(userEntity1).isNotNull();
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    // Should have already been tested.
    userEntity1 = new UserEntity();

    // Set.
    userEntity1.setId(1);
    userEntity1.setTypeId(TYPE_ID);
    userEntity1.setBranchId(1);
    userEntity1.setDisplayName(USER_NAME);
    userEntity1.setEmail(EMAIL);
    userEntity1.setPassword(FAKE_PASSWORD);
    userEntity1.setCreated(CREATED);
    userEntity1.setDeleted(null);
    userEntity1.setConfirmed(true);

    // Get.
    assertThat(userEntity1.getId()).isEqualTo(1);
    assertThat(userEntity1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(userEntity1.getBranchId()).isEqualTo(1);
    assertThat(userEntity1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(userEntity1.getEmail()).isEqualTo(EMAIL);
    assertThat(userEntity1.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(userEntity1.getCreated()).isEqualTo(CREATED);
    assertThat(userEntity1.getDeleted()).isNull();
    assertThat(userEntity1.getConfirmed()).isTrue();
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    userEntity1 = UserEntity.builder().id(1).typeId(TYPE_ID).branchId(1).displayName(USER_NAME).email(EMAIL)
                            .password(FAKE_PASSWORD).created(CREATED).deleted(null).confirmed(true).build();
    assertThat(userEntity1.getId()).isEqualTo(1);
    assertThat(userEntity1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(userEntity1.getBranchId()).isEqualTo(1);
    assertThat(userEntity1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(userEntity1.getEmail()).isEqualTo(EMAIL);
    assertThat(userEntity1.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(userEntity1.getCreated()).isEqualTo(CREATED);
    assertThat(userEntity1.getDeleted()).isNull();
    assertThat(userEntity1.getConfirmed()).isTrue();
  }

  @Test
  void test_CanCompareWithEquals()
  {
    // Note, they have different IDs, usernames, and emails.
    userEntity1 = new UserEntity(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true);
    userEntity2 = new UserEntity(2, TYPE_ID, 1, "Monkey", "monk@e.mail", FAKE_PASSWORD, CREATED, null, true);
    userEntity3 = new UserEntity(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true);
    assertThat(userEntity1).isNotEqualTo(userEntity2);
    assertThat(userEntity1).isEqualTo(userEntity3);
  }

  @Test
  void test_CanCompareWithHashCode()
  {
    // Note, they have different IDs, usernames, and emails.
    userEntity1 = new UserEntity(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true);
    userEntity2 = new UserEntity(2, TYPE_ID, 1,"Monkey", "monk@e.mail", FAKE_PASSWORD, CREATED, null, true);
    userEntity3 = new UserEntity(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true);
    assertThat(userEntity1.hashCode()).isNotEqualTo(userEntity2.hashCode());
    assertThat(userEntity1.hashCode()).isEqualTo(userEntity3.hashCode());
  }
}
