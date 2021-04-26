package org.ssor.boss.core.transfer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.entity.UserType;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class SecureUserDetailsTests
{
  static final String USER_NAME = "LeftRuleMatters";
  static final LocalDateTime CREATED = LocalDateTime.now();
  static SecureUserDetails secureUserDetails1;
  static SecureUserDetails secureUserDetails2;
  static SecureUserDetails secureUserDetails3;

  @AfterEach
  void tearDown()
  {
    secureUserDetails1 = null;
    secureUserDetails2 = null;
    secureUserDetails3 = null;
  }

  @Test
  void test_CanCreateWithoutParameters()
  {
    secureUserDetails1 = new SecureUserDetails();
    assertThat(secureUserDetails1).isNotNull();
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    secureUserDetails1 = new SecureUserDetails(1, UserType.USER_DEFAULT, 1, USER_NAME, CREATED);
    assertThat(secureUserDetails1).isNotNull();
    assertThat(secureUserDetails1.getId()).isEqualTo(1);
    Assertions.assertThat(secureUserDetails1.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(secureUserDetails1.getBranchId()).isEqualTo(1);
    assertThat(secureUserDetails1.getUsername()).isEqualTo(USER_NAME);
    assertThat(secureUserDetails1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCreateFromUser()
  {
    final var email = "john.christman@smoothstack.com";
    final var fakePassword = "D8A4D80f7C27F25067E56671B1AA4f7172E3C7418DE442fDF42fF49CF49FE20E";
    final var user = new User(1, UserType.USER_DEFAULT, 1, USER_NAME, email, fakePassword, CREATED, null, false, false);
    secureUserDetails1 = new SecureUserDetails(user);
    assertThat(secureUserDetails1).isNotNull();
    assertThat(secureUserDetails1.getId()).isEqualTo(1);
    Assertions.assertThat(secureUserDetails1.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(secureUserDetails1.getBranchId()).isEqualTo(1);
    assertThat(secureUserDetails1.getUsername()).isEqualTo(USER_NAME);
    assertThat(secureUserDetails1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    secureUserDetails1 = new SecureUserDetails();
    assertThat(secureUserDetails1).isNotNull();

    secureUserDetails1.setId(1);
    secureUserDetails1.setType(UserType.USER_DEFAULT);
    secureUserDetails1.setBranchId(1);
    secureUserDetails1.setUsername(USER_NAME);
    secureUserDetails1.setCreated(CREATED);
    assertThat(secureUserDetails1.getId()).isEqualTo(1);
    Assertions.assertThat(secureUserDetails1.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(secureUserDetails1.getBranchId()).isEqualTo(1);
    assertThat(secureUserDetails1.getUsername()).isEqualTo(USER_NAME);
    assertThat(secureUserDetails1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    secureUserDetails1 = SecureUserDetails.builder().id(1).type(UserType.USER_DEFAULT).branchId(1).username(USER_NAME)
                                          .created(CREATED).build();
    assertThat(secureUserDetails1).isNotNull();
    assertThat(secureUserDetails1.getId()).isEqualTo(1);
    Assertions.assertThat(secureUserDetails1.getType()).isEqualTo(UserType.USER_DEFAULT);
    assertThat(secureUserDetails1.getBranchId()).isEqualTo(1);
    assertThat(secureUserDetails1.getUsername()).isEqualTo(USER_NAME);
    assertThat(secureUserDetails1.getCreated()).isEqualTo(CREATED);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    secureUserDetails1 = new SecureUserDetails(1, UserType.USER_DEFAULT, 1, USER_NAME, CREATED);
    secureUserDetails2 = new SecureUserDetails(2, UserType.USER_DEFAULT, 1, "Fish", CREATED);
    secureUserDetails3 = new SecureUserDetails(1, UserType.USER_DEFAULT, 1, USER_NAME, CREATED);
    assertThat(secureUserDetails2).isNotEqualTo(secureUserDetails1);
    assertThat(secureUserDetails3).isEqualTo(secureUserDetails1);
  }

  @Test
  void test_CanCompareWithHashCode()
  {
    secureUserDetails1 = new SecureUserDetails(1, UserType.USER_DEFAULT, 1, USER_NAME, CREATED);
    secureUserDetails2 = new SecureUserDetails(2, UserType.USER_DEFAULT, 1, "Fish", CREATED);
    secureUserDetails3 = new SecureUserDetails(1, UserType.USER_DEFAULT, 1, USER_NAME, CREATED);
    assertThat(secureUserDetails2.hashCode()).isNotEqualTo(secureUserDetails1.hashCode());
    assertThat(secureUserDetails3.hashCode()).isEqualTo(secureUserDetails1.hashCode());
  }
}
