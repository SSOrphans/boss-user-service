package org.ssor.boss.core.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A data transfer object for the input of registering a new user.
 * <p>
 *   Registering a user takes place at the website public portal. A potential user enters their, to be made, profile
 *   information and clicks submit. A request is made to the gateway at <code>/api/v1/users</code> with a
 *   {@link RegisterUserInput} instance. The input is sent to the controller for users and then to the user service
 *   class where it will be processed and a new user will be registered. When successful, a {@link RegisterUserOutput}
 *   will be sent back through the gateway to the client.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserInput implements Serializable
{
  @Size(min = 2, max = 16)
  @Pattern(regexp = "^[a-zA-Z]+([_ -]?[a-zA-Z0-9])*$", message = "Please provide a valid username")
  @NotBlank(message = "Please provide a valid username")
  private String username;

  @Email(message = "Please provide a valid email")
  @NotBlank(message = "Please provide a valid email")
  private String email;

  @Size(min = 64, max = 64)
  @Pattern(regexp = "^(([0-9]+[a-fA-F]+)|([a-fA-F]+[0-9]+))+[0-9a-fA-F]*$")
  @NotBlank(message = "The provided password hash must not be blank")
  private String password;
}

