package org.ssor.boss.user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.CreateUserInputDTO;
import org.ssor.boss.user.dto.CreateUserResultDTO;
import org.ssor.boss.user.dto.ForgotPassEmailDto;
import org.ssor.boss.user.dto.ForgotPassTokenDto;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.dto.UserProfileDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.UserAlreadyExistsException;
import org.ssor.boss.user.exception.UserDataAccessException;
import org.ssor.boss.user.exception.ForgotPassTokenException;
import org.ssor.boss.user.repository.UserRepository;
import org.ssor.boss.user.security.JwtForgotPassToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	public static final String NULL_DTO_MESSAGE = "User data transfer object must not be null";
	public static final String NULL_LDT_MESSAGE = "User creation date time must not be null";
	public static final String NULL_USERNAME_MESSAGE = "User display name must not be null";
	public static final String NULL_EMAIL_MESSAGE = "User email must not be null";
	public static final String NULL_PASSWORD_MESSAGE = "User password must not be null";
	public static final String INVALID_USER_ID = "No such user with id: ";
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtForgotPassToken jwtForgotPassToken;

	public Iterable<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	public CreateUserResultDTO createUser(CreateUserInputDTO createUserInputDTO, LocalDateTime created)
			throws IllegalArgumentException {
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

	public UserEntity getUserWithId(final int id) {
		final var result = userRepository.findById(id);
		if (result.isEmpty())
			return null;
		return result.get();
	}

	public void updateUserWithId(final int id, CreateUserInputDTO createUserInputDTO) throws IllegalArgumentException {
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

	public void deleteUserWithId(final int id) {
		userRepository.deleteById(1);
	}

	public Optional<UserInfoDto> findUserById(Integer userId) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(userId);
			if (userEntity.isPresent() && userEntity.orElseThrow().getDeleted() == null) {
				UserInfoDto userInfoDto = UserInfoDto.builder()
						.userId(userEntity.map(user -> user.getId()).orElseThrow())
						.displayName(userEntity.map(user -> user.getDisplayName()).orElseThrow())
						.email(userEntity.map(user -> user.getEmail()).orElseThrow())
						.created(userEntity.map(user -> user.getCreated()).orElseThrow()).build();
				return Optional.ofNullable(userInfoDto);
			}
			return Optional.empty();
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex) {
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Optional<UserProfileDto> updateUserProfile(Integer userId, UserProfileDto userProfileDto) {
		try {
			Optional<UserEntity> userRepoEntity = userRepository.findById(userId);
			if (userRepoEntity.isPresent() && userRepoEntity.orElseThrow().getDeleted() == null
					&& userId.equals(userRepoEntity.orElseThrow().getId())) {
				UserEntity userEntity = userRepoEntity.get();
				if (userProfileDto.getDisplayName() != null && !userProfileDto.getDisplayName().isBlank())
					userEntity.setDisplayName(userProfileDto.getDisplayName());
				if (userProfileDto.getEmail() != null && !userProfileDto.getEmail().isBlank())
					userEntity.setEmail(userProfileDto.getEmail());
				if (userProfileDto.getPassword() != null && !userProfileDto.getPassword().isBlank())
					userEntity.setPassword(userProfileDto.getPassword());
				userRepository.save(userEntity);
				return Optional.ofNullable(userProfileDto);
			}
			return Optional.empty();
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex) {
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Boolean deleteUserAccount(Integer userId) {
		try {
			Optional<UserEntity> userRepoEntity = userRepository.findById(userId);
			if (userRepoEntity.isPresent() && userRepoEntity.orElseThrow().getDeleted() == null
					&& userId.equals(userRepoEntity.orElseThrow().getId())) {
				UserEntity userEntity = userRepoEntity.orElseThrow();
				userEntity.setDeleted(LocalDateTime.now());
				userRepository.save(userEntity);
				return true;
			}
			return false;
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex) {
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Boolean sendPasswordReset(ForgotPassEmailDto forgotPassEmailDto) {
		if (forgotPassEmailDto.getEmail() != null && userRepository.checkUserEmail(forgotPassEmailDto.getEmail())) {
			// generate token
			String token = jwtForgotPassToken.generateAccessToken(forgotPassEmailDto.getEmail());
			// TODO: send password reset to email with AWS SNS
			return true;
		}
		return false;
	}

	public Optional<ForgotPassTokenDto> updateForgotPassword(ForgotPassTokenDto forgotPassTokenDto) {
		try {
			if (jwtForgotPassToken.validate(forgotPassTokenDto.getToken())) {
				String userEmail = jwtForgotPassToken.getUserEmail(forgotPassTokenDto.getToken());
				Optional<UserEntity> userRepoEntity = userRepository.findUserByEmail(userEmail);
				if (userRepoEntity.isPresent() && userRepoEntity.orElseThrow().getDeleted() == null) {
					UserEntity userEntity = userRepoEntity.get();
					if (forgotPassTokenDto.getPassword() != null && !forgotPassTokenDto.getPassword().isBlank()) {
						userEntity.setPassword(forgotPassTokenDto.getPassword());
						userRepository.save(userEntity);
						return Optional.ofNullable(forgotPassTokenDto);
					}
				}
			}
			return Optional.empty();
		} catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			// TODO: log exception
			throw new ForgotPassTokenException("There is an issue validating the token. ");
		}
	}
}
