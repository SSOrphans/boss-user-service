package org.ssor.boss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInputDTO implements Serializable
{
  @JsonProperty(value = "displayName")
  private String displayName;

  @JsonProperty(value = "email")
  private String email;

  @JsonProperty(value = "password")
  private String password;
}
