package org.ssor.boss.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Describes a user with user details.
 * <p>
 *   Users are a construct designed to represent a person using an external API or the open web portal. Users, however,
 *   could also be an Administrator with extra permissions, or a Vendor with different types of permissions from default
 *   users.
 * </p>
 * <p>
 *   Default users have the ability to access the public portal of the website and make limited requests from the
 *   website. These requests include: creating a checking or saving account, obtaining a debit card or signing up for a
 *   credit card, asking for a loan of various types, updating their profile, deactivating a card, closing an account,
 *   paying off cards or loans, and many more things.
 * </p>
 * <p>
 *   Administrators are capable of doing everything the users can, to any user they wish and more. Administrators can
 *   promote default users to administrators, in the event a user becomes an employee, or demote other admins to default
 *   users if they get fired. Admins are also able to do management of accounts, cards, and loans for a branch in case
 *   something went wrong or there are mistakes. Admins are able to do things for users who may be locked out of their
 *   account for a reason as well.
 * </p>
 * <p>
 *   Vendors are a special type of user. They are users who can create vendor applications. These applications are pre-
 *   authorized with the gateway. This allows the vendor applications to make requests to various parts of the external
 *   APIs. They do not have full reign of the API, but are given access to parts of the API that normal users are not.
 *   Vendor applications, specifically, are capable of issuing transactions through our external APIs.
 * </p>
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "boss", uniqueConstraints = {
  @UniqueConstraint(columnNames = "id"),
  @UniqueConstraint(columnNames = "username"),
  @UniqueConstraint(columnNames = "email")
})
public class User implements UserDetails, Serializable
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Enumerated
  @Column(name = "type_id")
  private UserType type;
  @Column(name = "branch_id")
  private Integer branchId;
  private String username;
  private String email;
  private String password;
  private LocalDateTime created;
  private LocalDateTime deleted = null;
  private boolean enabled = false;
  private boolean locked = false;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    final var authority = new SimpleGrantedAuthority(type.name());
    return Collections.singletonList(authority);
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return deleted == null;
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(username, user.username) &&
           Objects.equals(email, user.email);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(id, username, email);
  }
}
