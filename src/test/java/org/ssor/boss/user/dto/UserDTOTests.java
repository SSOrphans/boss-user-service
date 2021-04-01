package org.ssor.boss.user.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDTOTests
{
  static UserDTO userDTO1;
  static UserDTO userDTO2;
  static UserDTO userDTO3;

  @AfterEach
  void teardown()
  {
    userDTO1 = null;
    userDTO2 = null;
    userDTO3 = null;
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    userDTO1 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(userDTO1).isNotNull();
    assertThat(userDTO1.getDisplayName()).isEqualTo("LeftRuleMatters");
    assertThat(userDTO1.getEmail()).isEqualTo("john.christman@smoothstack.com");
    assertThat(userDTO1.getPassword()).isEqualTo("password");
  }

  @Test
  void test_CanCompareWithEquals()
  {
    userDTO1 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    userDTO2 = new UserDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    userDTO3 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(userDTO1).isNotEqualTo(userDTO2);
    assertThat(userDTO1).isEqualTo(userDTO3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    userDTO1 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    userDTO2 = new UserDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    userDTO3 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertThat(userDTO1.hashCode()).isNotEqualTo(userDTO2.hashCode());
    assertThat(userDTO1.hashCode()).isEqualTo(userDTO3.hashCode());
  }
}
