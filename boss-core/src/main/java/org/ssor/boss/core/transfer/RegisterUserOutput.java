package org.ssor.boss.core.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A data transfer object for the result of registering a new user.
 * <p>
 *  After registering a new user from the website public portal, if the registration succeeds, this object will be
 *  returned. This data could be displayed for one reason or another, or ignored. It's at the discretion of the client.
 *  For the most part, it will be used to make sure the user was created correctly.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserOutput implements Serializable
{
  private int id;
  private int typeId;
  private int branchId;
  private String username;
  private String email;
  private LocalDateTime created;
}
