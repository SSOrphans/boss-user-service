package org.ssor.boss.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CreateUserResultDTO implements Serializable
{
  private final int id;
  private final String displayName;
  private final String email;
  private final LocalDateTime created;
}
