package org.ssor.boss.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateUserInputDTO implements Serializable
{
  private final String displayName;
  private final String email;
  private final String password;
}
