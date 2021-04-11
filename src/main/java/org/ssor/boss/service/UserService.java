package org.ssor.boss.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssor.boss.dto.CreateUserInputDTO;
import org.ssor.boss.dto.CreateUserResultDTO;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.entity.UserType;
import org.ssor.boss.exception.UserAlreadyExistsException;
import org.ssor.boss.repository.UserEntityRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
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
  private final UserEntityRepository userEntityRepository;

  public Iterable<UserEntity> getAllUsers()
  {
    return userEntityRepository.findAll();
  }

  public CreateUserResultDTO createUser(CreateUserInputDTO createUserInputDTO, LocalDateTime created) throws
    IllegalArgumentException, UserAlreadyExistsException
  {
    // Forward contract.
    if (createUserInputDTO == null)
      throw new IllegalArgumentException(NULL_DTO_MESSAGE);
    if (created == null)
      throw new IllegalArgumentException(NULL_LDT_MESSAGE);

    final var username = createUserInputDTO.getDisplayName();
    final var email = createUserInputDTO.getEmail();
    final var password = createUserInputDTO.getPassword();
    if (username == null)
      throw new IllegalArgumentException(NULL_USERNAME_MESSAGE);
    if (email == null)
      throw new IllegalArgumentException(NULL_EMAIL_MESSAGE);
    if (password == null)
      throw new IllegalArgumentException(NULL_PASSWORD_MESSAGE);

    if (userEntityRepository.checkUserExistsWithUsernameAndEmail(username, email))
      throw new UserAlreadyExistsException();

    // Action.
    final var typeId = UserType.DEFAULT.ordinal();
    final var user = new UserEntity(null, typeId, 1, username, email, password, created, null, false);
    final var result = userEntityRepository.save(user);

    // Create confirmation for User.
    return CreateUserResultDTO.builder()
                              .id(result.getId())
                              .typeId(result.getTypeId())
                              .branchId(result.getBranchId())
                              .displayName(result.getDisplayName())
                              .email(result.getEmail())
                              .created(result.getCreated())
                              .build();
  }

  public Optional<UserEntity> getUserWithId(final int id)
  {
    return userEntityRepository.findById(id);
  }

  public void updateUserWithId(final int id, CreateUserInputDTO createUserInputDTO) throws
    IllegalArgumentException
  {
    if (createUserInputDTO == null)
      throw new IllegalArgumentException(NULL_DTO_MESSAGE);

    final var possibleUser = getUserWithId(id);
    if (possibleUser.isEmpty())
      throw new IllegalArgumentException(INVALID_USER_ID + id);

    final var user = possibleUser.get();
    if (createUserInputDTO.getDisplayName() != null)
      user.setDisplayName(createUserInputDTO.getDisplayName());
    if (createUserInputDTO.getEmail() != null)
      user.setEmail(createUserInputDTO.getEmail());
    if (createUserInputDTO.getPassword() != null)
      user.setPassword(createUserInputDTO.getPassword());
    userEntityRepository.save(user);
  }

  public void deleteUserWithId(final int id)
  {
    userEntityRepository.deleteById(id);
  }
}
