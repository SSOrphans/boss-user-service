package org.ssor.boss.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserInputDTOTests
{
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
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    createUserInputDTO1 = new CreateUserInputDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(createUserInputDTO1).isNotNull();
    assertThat(createUserInputDTO1.getDisplayName()).isEqualTo("LeftRuleMatters");
    assertThat(createUserInputDTO1.getEmail()).isEqualTo("john.christman@smoothstack.com");
    assertThat(createUserInputDTO1.getPassword()).isEqualTo("password");
  }

  @Test
  void test_CanCompareWithEquals()
  {
    createUserInputDTO1 = new CreateUserInputDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    createUserInputDTO2 = new CreateUserInputDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    createUserInputDTO3 = new CreateUserInputDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(createUserInputDTO1).isNotEqualTo(createUserInputDTO2);
    assertThat(createUserInputDTO1).isEqualTo(createUserInputDTO3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    createUserInputDTO1 = new CreateUserInputDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    createUserInputDTO2 = new CreateUserInputDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    createUserInputDTO3 = new CreateUserInputDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(createUserInputDTO1.hashCode()).isNotEqualTo(createUserInputDTO2.hashCode());
    assertThat(createUserInputDTO1.hashCode()).isEqualTo(createUserInputDTO3.hashCode());
  }
}
