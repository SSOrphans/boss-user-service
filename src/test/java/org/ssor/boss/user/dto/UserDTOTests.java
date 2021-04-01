package org.ssor.boss.user.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    assertNotNull(userDTO1);
    assertEquals("LeftRuleMatters", userDTO1.getDisplayName());
    assertEquals("john.christman@smoothstack.com", userDTO1.getEmail());
    assertEquals("password", userDTO1.getPassword());
  }

  @Test
  void test_CanCompareWithEquals()
  {
    userDTO1 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    userDTO2 = new UserDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    userDTO3 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertNotEquals(userDTO2, userDTO1);
    assertEquals(userDTO3, userDTO1);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    userDTO1 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    userDTO2 = new UserDTO("SoraKatadzuma", "sorakatadzuma@gmail.com", "password");
    userDTO3 = new UserDTO("LeftRuleMatters", "john.christman@smoothstack.com", "password");
    assertNotEquals(userDTO2.hashCode(), userDTO1.hashCode());
    assertEquals(userDTO3.hashCode(), userDTO1.hashCode());
  }
}
