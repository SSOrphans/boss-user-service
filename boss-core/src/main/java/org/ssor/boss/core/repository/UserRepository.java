package org.ssor.boss.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssor.boss.core.entity.User;
import java.util.Optional;

/**
 * Defines a repository capable of performing actions on a database containing {@link User}s of a given schema.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
  /**
   * Gets a user by their username.
   *
   * @param username The username of the user.
   * @return The obtained user inside of the optional, else an empty optional.
   */
  Optional<User> getUserByUsername(String username);

  /**
   * Gets a user by their email.
   *
   * @param email The email of the user.
   * @return The obtained user inside of the optional, else an empty optional.
   */
  Optional<User> getUserByEmail(String email);

  /**
   * Gets a user by their username or email.
   *
   * @param username The username of the user.
   * @param email The email of the user.
   * @return The obtained user inside of the optional, else an empty optional.
   */
  Optional<User> getUserByUsernameOrEmail(String username, String email);

  /**
   * Checks if a user exists by a username alone.
   *
   * @param username The user name of the user.
   * @return True if the user exists, false otherwise.
   */
  boolean existsUserByUsername(String username);

  /**
   * Checks if a user exists by an email alone.
   *
   * @param email The email of the user.
   * @return True if the user exists, false otherwise.
   */
  boolean existsUserByEmail(String email);

  /**
   * Checks if a user exists by a username or an email.
   *
   * @param username The username of the user.
   * @param email The email of the user.
   * @return True if the user exists, false otherwise.
   */
  boolean existsUserByUsernameOrEmail(String username, String email);
}
