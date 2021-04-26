package org.ssor.boss.core.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Represents the data of a User that can be updated.
 * <p>
 *   User profiles contain their username, email, and password. User profiles don't typically contain their account
 *   holder information. This information should be updated or changed separately, if it is possible for a user to do so.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInput
{
  private int userId;

  // TODO:
  //   decide how and if we should allow users to update their branch.

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
