package org.ssor.boss.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserInputDTOTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final String EMAIL = "john.christman@smoothstack.com";
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";
  static CreateUserInputDTO createUserInputDTO1;
  static CreateUserInputDTO createUserInputDTO2;
  static CreateUserInputDTO createUserInputDTO3;

  @AfterEach
  void teardown()
  {
    createUserInputDTO1 = null;
    createUserInputDTO2 = null;
    createUserInputDTO3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    createUserInputDTO1 = new CreateUserInputDTO();
    assertThat(createUserInputDTO1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    createUserInputDTO1 = new CreateUserInputDTO(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(createUserInputDTO1).isNotNull();
    assertThat(createUserInputDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserInputDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserInputDTO1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    createUserInputDTO1 = new CreateUserInputDTO();
    createUserInputDTO1.setDisplayName(USER_NAME);
    createUserInputDTO1.setEmail(EMAIL);
    createUserInputDTO1.setPassword(FAKE_PASSWORD);
    assertThat(createUserInputDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserInputDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserInputDTO1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    createUserInputDTO1 = CreateUserInputDTO.builder().displayName(USER_NAME).email(EMAIL).password(FAKE_PASSWORD)
                                            .build();
    assertThat(createUserInputDTO1).isNotNull();
    assertThat(createUserInputDTO1.getDisplayName()).isEqualTo(USER_NAME);
    assertThat(createUserInputDTO1.getEmail()).isEqualTo(EMAIL);
    assertThat(createUserInputDTO1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    createUserInputDTO1 = new CreateUserInputDTO(USER_NAME, EMAIL, FAKE_PASSWORD);
    createUserInputDTO2 = new CreateUserInputDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    createUserInputDTO3 = new CreateUserInputDTO(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(createUserInputDTO1).isNotEqualTo(createUserInputDTO2);
    assertThat(createUserInputDTO1).isEqualTo(createUserInputDTO3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    createUserInputDTO1 = new CreateUserInputDTO(USER_NAME, EMAIL, FAKE_PASSWORD);
    createUserInputDTO2 = new CreateUserInputDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    createUserInputDTO3 = new CreateUserInputDTO(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(createUserInputDTO1.hashCode()).isNotEqualTo(createUserInputDTO2.hashCode());
    assertThat(createUserInputDTO1.hashCode()).isEqualTo(createUserInputDTO3.hashCode());
  }
}
