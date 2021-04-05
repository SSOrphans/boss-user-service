package org.ssor.boss.exception;

public class UserAlreadyExistsException extends RuntimeException
{
  public static final String USER_TAKEN_MESSAGE = "User with email or username already taken";

  public UserAlreadyExistsException()
  {
    super(USER_TAKEN_MESSAGE);
  }
}
