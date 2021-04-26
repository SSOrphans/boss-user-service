package org.ssor.boss.core.transfer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisterUserInputTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final String EMAIL = "john.christman@smoothstack.com";
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";
  static RegisterUserInput registerUserInput1;
  static RegisterUserInput registerUserInput2;
  static RegisterUserInput registerUserInput3;

  @AfterEach
  void tearDown()
  {
    registerUserInput1 = null;
    registerUserInput2 = null;
    registerUserInput3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    registerUserInput1 = new RegisterUserInput();
    assertThat(registerUserInput1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    registerUserInput1 = new RegisterUserInput(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(registerUserInput1).isNotNull();
    assertThat(registerUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    registerUserInput1 = new RegisterUserInput();
    registerUserInput1.setUsername(USER_NAME);
    registerUserInput1.setEmail(EMAIL);
    registerUserInput1.setPassword(FAKE_PASSWORD);
    assertThat(registerUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    registerUserInput1 = RegisterUserInput.builder().username(USER_NAME).email(EMAIL).password(FAKE_PASSWORD)
                                          .build();
    assertThat(registerUserInput1).isNotNull();
    assertThat(registerUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    registerUserInput1 = new RegisterUserInput(USER_NAME, EMAIL, FAKE_PASSWORD);
    registerUserInput2 = new RegisterUserInput("SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    registerUserInput3 = new RegisterUserInput(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(registerUserInput1).isNotEqualTo(registerUserInput2);
    assertThat(registerUserInput1).isEqualTo(registerUserInput3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    registerUserInput1 = new RegisterUserInput(USER_NAME, EMAIL, FAKE_PASSWORD);
    registerUserInput2 = new RegisterUserInput("SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    registerUserInput3 = new RegisterUserInput(USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(registerUserInput1.hashCode()).isNotEqualTo(registerUserInput2.hashCode());
    assertThat(registerUserInput1.hashCode()).isEqualTo(registerUserInput3.hashCode());
  }
}
