package org.ssor.boss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInputDTO implements Serializable
{
  @Size(min = 2, max = 16)
  @NotBlank(message = "Please provide a valid username")
  @JsonProperty(value = "displayName")
  private String displayName;

  @Email
  @NotBlank(message = "Please provide a valid email")
  @JsonProperty(value = "email")
  private String email;

  @Size(min = 64, max = 64)
  @NotBlank(message = "The provided password hash must not be blank")
  @JsonProperty(value = "password")
  private String password;
}
