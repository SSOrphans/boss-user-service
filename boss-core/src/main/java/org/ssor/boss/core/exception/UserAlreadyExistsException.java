package org.ssor.boss.core.exception;

/**
 * An exception thrown when attempting to register a user, but the user already exists.
 * <p>
 *   A user already exists if the username or email is already in use.
 * </p>
 */
public class UserAlreadyExistsException extends RuntimeException
{
  public UserAlreadyExistsException()
  {
    super("User with username or email already exists");
  }
}
