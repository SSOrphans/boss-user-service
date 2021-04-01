package org.ssor.boss.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.CreateUserInputDTO;
import org.ssor.boss.user.dto.CreateUserResultDTO;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class UserService
{
  public static final String NULL_DTO_MESSAGE = "User data transfer object must not be null";
  public static final String NULL_LDT_MESSAGE = "User creation date time must not be null";
  public static final String NULL_USERNAME_MESSAGE = "User display name must not be null";
  public static final String NULL_EMAIL_MESSAGE = "User email must not be null";
  public static final String NULL_PASSWORD_MESSAGE = "User password must not be null";
  public static final String USER_TAKEN_MESSAGE = "User with email or username already taken";
  private final UserRepository userRepository;

  public Iterable<UserEntity> getAllUsers()
  {
    return userRepository.findAll();
  }

  public CreateUserResultDTO createUser(CreateUserInputDTO createUserInputDTO, LocalDateTime created) throws
    IllegalArgumentException
  {
    // Forward contract.
    if (createUserInputDTO == null)
      throw new IllegalArgumentException(NULL_DTO_MESSAGE);
    if (created == null)
      throw new IllegalArgumentException(NULL_LDT_MESSAGE);
    if (createUserInputDTO.getDisplayName() == null)
      throw new IllegalArgumentException(NULL_USERNAME_MESSAGE);
    if (createUserInputDTO.getEmail() == null)
      throw new IllegalArgumentException(NULL_EMAIL_MESSAGE);
    if (createUserInputDTO.getPassword() == null)
      throw new IllegalArgumentException(NULL_PASSWORD_MESSAGE);

    final var matchingEntity = new UserEntity();
    matchingEntity.setDisplayName(createUserInputDTO.getDisplayName());
    matchingEntity.setEmail(createUserInputDTO.getEmail());

    final var matcher = ExampleMatcher.matchingAny().withMatcher("display_name", exact())
                                      .withMatcher("email", exact());
    final var example = Example.of(matchingEntity, matcher);
    final var exists = userRepository.exists(example);
    if (exists) // TODO: Replace with UserAlreadyExistsException.
      throw new IllegalArgumentException(USER_TAKEN_MESSAGE);

    // Action.
    final var user = new UserEntity(null, createUserInputDTO.getDisplayName(), createUserInputDTO.getEmail(),
                                    createUserInputDTO.getPassword(), created, null, false);
    final var result = userRepository.save(user);
    return new CreateUserResultDTO(result.getId(), result.getDisplayName(), result.getEmail(), result.getCreated());
  }

  public UserEntity getUserWithId(final int id)
  {
    final var result = userRepository.findById(id);
    if (result.isEmpty())
      return null;
    return result.get();
  }

  public void updateUserWithId(final int id, CreateUserInputDTO createUserInputDTO) throws
    IllegalArgumentException
  {
    if (createUserInputDTO == null)
      throw new IllegalArgumentException(NULL_DTO_MESSAGE);
    final var user = getUserWithId(id);
    if (user == null)
      throw new IllegalArgumentException(String.format("No such user with id: %d", id));
    if (createUserInputDTO.getDisplayName() != null)
      user.setDisplayName(createUserInputDTO.getDisplayName());
    if (createUserInputDTO.getEmail() != null)
      user.setEmail(createUserInputDTO.getEmail());
    if (createUserInputDTO.getPassword() != null)
      user.setPassword(createUserInputDTO.getPassword());
    userRepository.save(user);
  }

  public void deleteUserWithId(final int id)
  {
    userRepository.deleteById(1);
  }
}
