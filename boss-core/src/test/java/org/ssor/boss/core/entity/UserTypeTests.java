package org.ssor.boss.core.entity;

import org.junit.jupiter.api.Test;
import static org.ssor.boss.core.entity.UserType.USER_ADMIN;
import static org.ssor.boss.core.entity.UserType.USER_DEFAULT;
import static org.ssor.boss.core.entity.UserType.USER_GUEST;
import static org.ssor.boss.core.entity.UserType.USER_HOLDER;
import static org.ssor.boss.core.entity.UserType.USER_UNKNOWN;
import static org.ssor.boss.core.entity.UserType.USER_VENDOR;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTypeTests
{
  @Test
  void test_CanGetIndexOfUserType()
  {
    assertThat(USER_UNKNOWN.index()).isEqualTo(0);
    assertThat(USER_GUEST.index()).isEqualTo(1);
    assertThat(USER_DEFAULT.index()).isEqualTo(2);
    assertThat(USER_HOLDER.index()).isEqualTo(3);
    assertThat(USER_VENDOR.index()).isEqualTo(4);
    assertThat(USER_ADMIN.index()).isEqualTo(5);
  }

  @Test
  void test_CanConvertEnumerationToString()
  {
    assertThat(USER_UNKNOWN.toString()).isEqualTo("USER_UNKNOWN");
    assertThat(USER_GUEST.toString()).isEqualTo("USER_GUEST");
    assertThat(USER_DEFAULT.toString()).isEqualTo("USER_DEFAULT");
    assertThat(USER_HOLDER.toString()).isEqualTo("USER_HOLDER");
    assertThat(USER_VENDOR.toString()).isEqualTo("USER_VENDOR");
    assertThat(USER_ADMIN.toString()).isEqualTo("USER_ADMIN");
  }
}
