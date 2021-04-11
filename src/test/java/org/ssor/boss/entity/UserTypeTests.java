package org.ssor.boss.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTypeTests
{
  @Test
  void test_UserTypeToStringReturnsAppropriateValues()
  {
    assertThat(UserType.UNKNOWN.toString()).isEqualTo("USER_UNKNOWN");
    assertThat(UserType.GUEST.toString()).isEqualTo("USER_GUEST");
    assertThat(UserType.DEFAULT.toString()).isEqualTo("USER_DEFAULT");
    assertThat(UserType.HOLDER.toString()).isEqualTo("USER_HOLDER");
    assertThat(UserType.ADMIN.toString()).isEqualTo("USER_ADMIN");
    assertThat(UserType.VENDOR.toString()).isEqualTo("USER_VENDOR");
  }
}
