package org.ssor.boss.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResultDTO implements Serializable
{
  @JsonProperty(value = "id")
  private int id;

  @JsonProperty(value = "displayName")
  private String displayName;

  @JsonProperty(value = "email")
  private String email;

  @JsonProperty(value = "created")
  private LocalDateTime created;
}
