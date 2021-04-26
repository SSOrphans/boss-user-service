package org.ssor.boss.core.transfer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.ssor.boss.core.entity.UserType;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisterUserOutputTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final String EMAIL = "john.christman@smoothstack.com";
  static final LocalDateTime CREATED = LocalDateTime.now();
  static final Integer TYPE_ID = UserType.USER_DEFAULT.index();
  static RegisterUserOutput registerUserOutput1;
  static RegisterUserOutput registerUserOutput2;
  static RegisterUserOutput registerUserOutput3;

  @AfterEach
  void tearDown()
  {
    registerUserOutput1 = null;
    registerUserOutput2 = null;
    registerUserOutput3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    registerUserOutput1 = new RegisterUserOutput();
    assertThat(registerUserOutput1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    registerUserOutput1 = new RegisterUserOutput(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(registerUserOutput1).isNotNull();
    assertThat(registerUserOutput1.getId()).isEqualTo(1);
    assertThat(registerUserOutput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserOutput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserOutput1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    registerUserOutput1 = new RegisterUserOutput();
    registerUserOutput1.setId(1);
    registerUserOutput1.setTypeId(TYPE_ID);
    registerUserOutput1.setBranchId(1);
    registerUserOutput1.setUsername(USER_NAME);
    registerUserOutput1.setEmail(EMAIL);
    registerUserOutput1.setCreated(CREATED);
    assertThat(registerUserOutput1.getId()).isEqualTo(1);
    assertThat(registerUserOutput1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(registerUserOutput1.getBranchId()).isEqualTo(1);
    assertThat(registerUserOutput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserOutput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserOutput1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    registerUserOutput1 = RegisterUserOutput.builder().id(1).typeId(TYPE_ID).branchId(1).username(USER_NAME)
                                              .email(EMAIL).created(CREATED).build();
    assertThat(registerUserOutput1.getId()).isEqualTo(1);
    assertThat(registerUserOutput1.getTypeId()).isEqualTo(TYPE_ID);
    assertThat(registerUserOutput1.getBranchId()).isEqualTo(1);
    assertThat(registerUserOutput1.getUsername()).isEqualTo(USER_NAME);
    assertThat(registerUserOutput1.getEmail()).isEqualTo(EMAIL);
    assertThat(registerUserOutput1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    registerUserOutput1 = new RegisterUserOutput(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    registerUserOutput2 = new RegisterUserOutput(2, TYPE_ID, 1, "SoraKatadzuma", "sorakatadzuma@gmail.com", CREATED);
    registerUserOutput3 = new RegisterUserOutput(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(registerUserOutput1).isNotEqualTo(registerUserOutput2);
    assertThat(registerUserOutput1).isEqualTo(registerUserOutput3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    registerUserOutput1 = new RegisterUserOutput(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    registerUserOutput2 = new RegisterUserOutput(2, TYPE_ID, 1, "SoraKatadzuma", "sorakatadzuma@gmail.com", CREATED);
    registerUserOutput3 = new RegisterUserOutput(1, TYPE_ID, 1, USER_NAME, EMAIL, CREATED);
    assertThat(registerUserOutput1.hashCode()).isNotEqualTo(registerUserOutput2.hashCode());
    assertThat(registerUserOutput1.hashCode()).isEqualTo(registerUserOutput3.hashCode());
  }
}
