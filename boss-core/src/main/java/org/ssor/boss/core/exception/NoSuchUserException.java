package org.ssor.boss.core.exception;

/**
 * An exception thrown when attempting to get a user results in no content.
 * <p>
 *   This exception helps tell the client application that a user with a given user id does not exist.
 * </p>
 */
public class NoSuchUserException extends RuntimeException
{
  public NoSuchUserException(int userId)
  {
    super(String.format("No such user with id %d", userId));
  }
}
