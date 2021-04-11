package org.ssor.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInputDTO implements Serializable
{
  @Size(min = 2, max = 16)
  @Pattern(regexp = "^[a-zA-Z]+([_ -]?[a-zA-Z0-9])*$", message = "Please provide a valid username")
  @NotBlank(message = "Please provide a valid username")
  private String displayName;

  @Email(message = "Please provide a valid email")
  @NotBlank(message = "Please provide a valid email")
  private String email;

  @Size(min = 64, max = 64)
  @Pattern(regexp = "^[A-Fa-f0-9]{64}$")
  @NotBlank(message = "The provided password hash must not be blank")
  private String password;
}
