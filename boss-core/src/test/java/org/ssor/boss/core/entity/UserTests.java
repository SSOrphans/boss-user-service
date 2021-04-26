package org.ssor.boss.core.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import static org.ssor.boss.core.entity.UserType.USER_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

// TODO:
//   assert user contains AUTHORITY. I had an issue with this when I tried, maybe someone else can get it to work.
public class UserTests
{
  static final UserType TYPE_ID = USER_DEFAULT;
  static final String USER_NAME = "username";
  static final String EMAIL = "me@example.com";
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";
  static final LocalDateTime CREATED = LocalDateTime.now();
  static final GrantedAuthority AUTHORITY = new SimpleGrantedAuthority(TYPE_ID.name());

  static User user1;
  static User user2;
  static User user3;

  @AfterEach
  void teardown()
  {
    user1 = null;
    user2 = null;
    user3 = null;
  }

  @Test
  void test_CanConstructWithoutParameters()
  {
    user1 = new User();
    assertThat(user1).isNotNull();
  }

  @Test
  void test_CanConstructWithAllParameters()
  {
    user1 = new User(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true, false);
    assertThat(user1).isNotNull();
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    // Should have already been tested.
    user1 = new User();
    assertThat(user1).isNotNull();

    // Set.
    user1.setId(1);
    user1.setType(TYPE_ID);
    user1.setBranchId(1);
    user1.setUsername(USER_NAME);
    user1.setEmail(EMAIL);
    user1.setPassword(FAKE_PASSWORD);
    user1.setCreated(CREATED);
    user1.setDeleted(null);
    user1.setEnabled(true);
    user1.setLocked(false);

    // Get.
    assertThat(user1.getId()).isEqualTo(1);
    assertThat(user1.getType()).isEqualTo(TYPE_ID);
    assertThat(user1.getBranchId()).isEqualTo(1);
    assertThat(user1.getUsername()).isEqualTo(USER_NAME);
    assertThat(user1.getEmail()).isEqualTo(EMAIL);
    assertThat(user1.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(user1.getCreated()).isEqualTo(CREATED);
    assertThat(user1.getDeleted()).isNull();
    assertThat(user1.isEnabled()).isTrue();
    assertThat(user1.isLocked()).isFalse();
    assertThat(user1.isAccountNonExpired()).isTrue();
    assertThat(user1.isAccountNonLocked()).isTrue();
    assertThat(user1.isCredentialsNonExpired()).isTrue();
  }

  @Test
  void test_CanCreateWithBuilder()
  {
    user1 = User.builder().id(1).type(TYPE_ID).branchId(1).username(USER_NAME).email(EMAIL).password(FAKE_PASSWORD)
                .created(CREATED).deleted(null).enabled(true).locked(false).build();
    assertThat(user1.getId()).isEqualTo(1);
    assertThat(user1.getType()).isEqualTo(TYPE_ID);
    assertThat(user1.getBranchId()).isEqualTo(1);
    assertThat(user1.getUsername()).isEqualTo(USER_NAME);
    assertThat(user1.getEmail()).isEqualTo(EMAIL);
    assertThat(user1.getPassword()).isEqualTo(FAKE_PASSWORD);
    assertThat(user1.getCreated()).isEqualTo(CREATED);
    assertThat(user1.getDeleted()).isNull();
    assertThat(user1.isEnabled()).isTrue();
    assertThat(user1.isLocked()).isFalse();
    assertThat(user1.isAccountNonExpired()).isTrue();
    assertThat(user1.isAccountNonLocked()).isTrue();
    assertThat(user1.isCredentialsNonExpired()).isTrue();
  }

  @Test
  void test_CanCompareWithEquals()
  {
    // Note, they have different IDs, usernames, and emails.
    user1 = new User(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true, false);
    user2 = new User(2, TYPE_ID, 1, "Monkey", "monk@e.mail", FAKE_PASSWORD, CREATED, null, true, false);
    user3 = new User(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true, false);
    assertThat(user1).isNotEqualTo(user2);
    assertThat(user1).isEqualTo(user3);
  }

  @Test
  void test_CanCompareWithHashCode()
  {
    // Note, they have different IDs, usernames, and emails.
    user1 = new User(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true, false);
    user2 = new User(2, TYPE_ID, 1,"Monkey", "monk@e.mail", FAKE_PASSWORD, CREATED, null, true, false);
    user3 = new User(1, TYPE_ID, 1, USER_NAME, EMAIL, FAKE_PASSWORD, CREATED, null, true, false);
    assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
    assertThat(user1.hashCode()).isEqualTo(user3.hashCode());
  }
}
