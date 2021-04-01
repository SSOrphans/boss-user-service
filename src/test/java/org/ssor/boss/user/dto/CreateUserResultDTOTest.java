package org.ssor.boss.user.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserResultDTOTest
{
  static CreateUserResultDTO createUserResultDTO1;
  static CreateUserResultDTO createUserResultDTO2;
  static CreateUserResultDTO createUserResultDTO3;

  @AfterEach
  void teardown()
  {
    createUserResultDTO1 = null;
    createUserResultDTO2 = null;
    createUserResultDTO3 = null;
  }

  @Test
  void test_CanCreateWithAllParametersAndGetProperties()
  {
    final var created = LocalDateTime.now();
    createUserResultDTO1 = new CreateUserResultDTO(1, "LeftRuleMatters", "john.christman@smoothstack.com",
                                                   created);
    assertThat(createUserResultDTO1).isNotNull();
    assertThat(createUserResultDTO1.getId()).isEqualTo(1);
    assertThat(createUserResultDTO1.getDisplayName()).isEqualTo("LeftRuleMatters");
    assertThat(createUserResultDTO1.getEmail()).isEqualTo("john.christman@smoothstack.com");
    assertThat(createUserResultDTO1.getCreated()).isEqualTo(created);
  }

  @Test
  void test_CanCompareWithEquals()
  {
    final var created = LocalDateTime.now();
    createUserResultDTO1 = new CreateUserResultDTO(1, "LeftRuleMatters", "john.christman@smoothstack.com", created);
    createUserResultDTO2 = new CreateUserResultDTO(2, "SoraKatadzuma", "sorakatadzuma@gmail.com", created);
    createUserResultDTO3 = new CreateUserResultDTO(1, "LeftRuleMatters", "john.christman@smoothstack.com", created);
    assertThat(createUserResultDTO1).isNotEqualTo(createUserResultDTO2);
    assertThat(createUserResultDTO1).isEqualTo(createUserResultDTO3);
  }

  @Test
  void test_CanCompareWithHashcode()
  {
    final var created = LocalDateTime.now();
    createUserResultDTO1 = new CreateUserResultDTO(1, "LeftRuleMatters", "john.christman@smoothstack.com", created);
    createUserResultDTO2 = new CreateUserResultDTO(2, "SoraKatadzuma", "sorakatadzuma@gmail.com", created);
    createUserResultDTO3 = new CreateUserResultDTO(1, "LeftRuleMatters", "john.christman@smoothstack.com", created);
    assertThat(createUserResultDTO1.hashCode()).isNotEqualTo(createUserResultDTO2.hashCode());
    assertThat(createUserResultDTO1.hashCode()).isEqualTo(createUserResultDTO3.hashCode());
  }
}
