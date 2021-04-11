package org.ssor.boss.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.ssor.boss.entity.UserType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: Test builder
class CreateUserResultDTOTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final String EMAIL = "john.christman@smoothstack.com";
  static final LocalDateTime CREATED = LocalDateTime.now();
  static final Integer TYPE_ID = UserType.DEFAULT.ordinal();
  static CreateUserResultDTO createUserResultDTO1;
  static CreateUserResultDTO createUserResultDTO2;
  static CreateUserResultDTO createUserResultDTO3;

  @AfterEach
  void teardown()
  {
    createUserResultDTO1 = null;
    createUserResultDTO2 = null;
    createUserResultDTO3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    createUserResultDTO1 = new CreateUserResultDTO();
    assertThat(createUserResultDTO1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    createUserResultDTO1 = new CreateUserResultDTO(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(createUserResultDTO1).isNotNull();
    assertThat(createUserResultDTO1.getId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserResultDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserResultDTO1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    createUserResultDTO1 = new CreateUserResultDTO();
    createUserResultDTO1.setId(1);
    createUserResultDTO1.setTypeId(TYPE_ID);
    createUserResultDTO1.setBranchId(1);
    createUserResultDTO1.setDisplayName(USER_NAME);
    createUserResultDTO1.setEmail(EMAIL);
    createUserResultDTO1.setCreated(CREATED);
    assertThat(createUserResultDTO1.getId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(createUserResultDTO1.getBranchId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserResultDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserResultDTO1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    createUserResultDTO1 = CreateUserResultDTO.builder().id(1).typeId(TYPE_ID).branchId(1).displayName(USER_NAME)
                                              .email(EMAIL).created(CREATED).build();
    assertThat(createUserResultDTO1.getId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(createUserResultDTO1.getBranchId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserResultDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserResultDTO1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    createUserResultDTO1 = new CreateUserResultDTO(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    createUserResultDTO2 = new CreateUserResultDTO(2, TYPE_ID, 1, "SoraKatadzuma", "sorakatadzuma@gmail.com", CREATED);
    createUserResultDTO3 = new CreateUserResultDTO(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(createUserResultDTO1).isNotEqualTo(createUserResultDTO2);
    assertThat(createUserResultDTO1).isEqualTo(createUserResultDTO3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    createUserResultDTO1 = new CreateUserResultDTO(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    createUserResultDTO2 = new CreateUserResultDTO(2, TYPE_ID, 1, "SoraKatadzuma", "sorakatadzuma@gmail.com", CREATED);
    createUserResultDTO3 = new CreateUserResultDTO(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(createUserResultDTO1.hashCode()).isNotEqualTo(createUserResultDTO2.hashCode());
    assertThat(createUserResultDTO1.hashCode()).isEqualTo(createUserResultDTO3.hashCode());
  }
}
