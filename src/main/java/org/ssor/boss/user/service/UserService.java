package org.ssor.boss.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.UserDTO;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService
{
  private final UserRepository userRepository;

  public Iterable<UserEntity> getAllUsers()
  {
    return userRepository.findAll();
  }

  public UserEntity getUserWithId(final int id)
  {
    final var result = userRepository.findById(id);
    if (result.isEmpty())
      return null;
    return result.get();
  }

  public void updateUserWithId(final int id, UserDTO userDTO)
    throws NullPointerException
  {
    final var user = getUserWithId(id);
    if (user == null)
      throw new IllegalArgumentException(String.format("No such user with id: %d", id));

    if (userDTO.getDisplayName() != null)
      user.setDisplayName(userDTO.getDisplayName());
    if (userDTO.getEmail() != null)
      user.setEmail(userDTO.getEmail());
    if (userDTO.getPassword() != null)
      user.setPassword(userDTO.getPassword());
    userRepository.save(user);
  }

  public void deleteUserWithId(final int id)
  {
    userRepository.deleteById(1);
  }
}
