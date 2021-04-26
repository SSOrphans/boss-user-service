package org.ssor.boss.core.transfer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserInputTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final String EMAIL = "john.christman@smoothstack.com";
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";
  static UpdateUserInput updateUserInput1;
  static UpdateUserInput updateUserInput2;
  static UpdateUserInput updateUserInput3;

  @AfterEach
  void tearDown()
  {
    updateUserInput1 = null;
    updateUserInput2 = null;
    updateUserInput3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    updateUserInput1 = new UpdateUserInput();
    assertThat(updateUserInput1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    updateUserInput1 = new UpdateUserInput(1, USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(updateUserInput1).isNotNull();
    assertThat(updateUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(updateUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(updateUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    updateUserInput1 = new UpdateUserInput();
    assertThat(updateUserInput1).isNotNull();

    updateUserInput1.setUserId(1);
    updateUserInput1.setUsername(USER_NAME);
    updateUserInput1.setEmail(EMAIL);
    updateUserInput1.setPassword(FAKE_PASSWORD);
    assertThat(updateUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(updateUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(updateUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    updateUserInput1 = UpdateUserInput.builder().userId(1).username(USER_NAME).email(EMAIL).password(FAKE_PASSWORD)
                                      .build();
    assertThat(updateUserInput1).isNotNull();
    assertThat(updateUserInput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(updateUserInput1.getEmail()).isEqualTo(EMAIL);
    assertThat(updateUserInput1.getPassword()).isEqualTo(FAKE_PASSWORD);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    updateUserInput1 = new UpdateUserInput(1, USER_NAME, EMAIL, FAKE_PASSWORD);
    updateUserInput2 = new UpdateUserInput(2, "SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    updateUserInput3 = new UpdateUserInput(1, USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(updateUserInput1).isNotEqualTo(updateUserInput2);
    assertThat(updateUserInput1).isEqualTo(updateUserInput3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    updateUserInput1 = new UpdateUserInput(1, USER_NAME, EMAIL, FAKE_PASSWORD);
    updateUserInput2 = new UpdateUserInput(2, "SoraKatadzuma", "sorakatadzuma@gmail.com", FAKE_PASSWORD);
    updateUserInput3 = new UpdateUserInput(1, USER_NAME, EMAIL, FAKE_PASSWORD);
    assertThat(updateUserInput1.hashCode()).isNotEqualTo(updateUserInput2.hashCode());
    assertThat(updateUserInput1.hashCode()).isEqualTo(updateUserInput3.hashCode());
  }
}
