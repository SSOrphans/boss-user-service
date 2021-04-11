package org.ssor.boss.entity;

import lombok.Getter;

@Getter
public enum UserType
{
  UNKNOWN,
  GUEST,
  DEFAULT,
  HOLDER,
  ADMIN,
  VENDOR;

  @Override
  public String toString()
  {
    String result = "USER_UNKNOWN";
    switch (this)
    {
      case GUEST: result = "USER_GUEST"; break;
      case DEFAULT: result = "USER_DEFAULT"; break;
      case HOLDER: result = "USER_HOLDER"; break;
      case ADMIN: result = "USER_ADMIN"; break;
      case VENDOR: result = "USER_VENDOR"; break;
    }

    return result;
  }
}
