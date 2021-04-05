package org.ssor.boss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ssor.boss.dto.CreateUserInputDTO;
import org.ssor.boss.dto.CreateUserResultDTO;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.exception.UserAlreadyExistsException;
import org.ssor.boss.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService
{
  public static final String NULL_DTO_MESSAGE = "User data transfer object must not be null";
  public static final String NULL_LDT_MESSAGE = "User creation date time must not be null";
  public static final String NULL_USERNAME_MESSAGE = "User display name must not be null";
  public static final String NULL_EMAIL_MESSAGE = "User email must not be null";
  public static final String NULL_PASSWORD_MESSAGE = "User password must not be null";
  public static final String INVALID_USER_ID = "No such user with id: ";
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

    if (userRepository.checkUserExists(createUserInputDTO.getDisplayName(), createUserInputDTO.getEmail()))
      throw new UserAlreadyExistsException();

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
      throw new IllegalArgumentException(INVALID_USER_ID + id);

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
