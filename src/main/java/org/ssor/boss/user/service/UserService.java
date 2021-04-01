package org.ssor.boss.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}
