package org.ssor.boss.core.entity;

/**
 * Describes the type of a user.
 * <p>
 *   <code>UserType</code> is another way of looking at the roles of a user. They are used to determine the permissions
 *   that users have. The permissions are used to describe to spring security what the user should be able to do when
 *   accessing a resource.
 * </p>
 */
public enum UserType
{
  USER_UNKNOWN,
  USER_GUEST,
  USER_DEFAULT,
  USER_HOLDER,
  USER_VENDOR,
  USER_ADMIN;

  /**
   * Simplified and more intuitive naming scheme for the enumeration value.
   * <p>
   *   Wraps the {@link #ordinal()} function, because Java thought it was a good meme to name the function something
   *   that most people will not understand, and then provide documentation about the function that only vaguely makes
   *   sense.
   * </p>
   *
   * @return The ordinal number of the enumeration.
   */
  public int index()
  {
    return ordinal();
  }

  /**
   * Gets the appropriate name for the different enumerations.
   * <p>
   *   Each enumeration represents an entry within the <code>user_roles</code> table of the <i>boss</i> database. The
   *   produced name directly reflects the name of the role of the user. This is important for checking and setting
   *   security permissions of the user.
   * </p>
   *
   * @return The generated string for the user type.
   */
  @Override
  public String toString()
  {
    String result = "";
    switch (this)
    {
      case USER_UNKNOWN:
        result = "USER_UNKNOWN"; break;
      case USER_GUEST:
        result = "USER_GUEST"; break;
      case USER_DEFAULT:
        result = "USER_DEFAULT"; break;
      case USER_HOLDER:
        result = "USER_HOLDER"; break;
      case USER_VENDOR:
        result = "USER_VENDOR"; break;
      case USER_ADMIN:
        result = "USER_ADMIN"; break;
    }

    return result;
  }
}
