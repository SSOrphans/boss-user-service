package org.ssor.boss.core.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.entity.UserType;
import java.time.LocalDateTime;

/**
 * Provides details about a user in a safe manner.
 * <p>
 *   Because vendors can get information about a user, we must provide a secure way of giving them that information. The
 *   information is generally limited. The results of getting a secure user is mostly for checking that a user exists,
 *   more so than getting that user's information.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecureUserDetails
{
  private int id;
  private UserType type;
  private int branchId;
  private String username;
  private LocalDateTime created;

  /**
   * Create {@link SecureUserDetails} from a {@link User}.
   *
   * @param user The user to create from.
   */
  public SecureUserDetails(User user)
  {
    id = user.getId();
    type = user.getType();
    branchId = user.getBranchId();
    username = user.getUsername();
    created = user.getCreated();
  }
}
