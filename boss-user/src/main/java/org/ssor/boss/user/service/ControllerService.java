package org.ssor.boss.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.exception.ForgotPassTokenException;
import org.ssor.boss.core.exception.UserDataAccessException;
import org.ssor.boss.core.repository.UserRepository;
import org.ssor.boss.core.transfer.UpdateUserInput;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UserInfoOutput;
import org.ssor.boss.user.security.JwtForgotPassToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ControllerService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtForgotPassToken jwtForgotPassToken;

	public Optional<UserInfoOutput> getUserInfo(Integer userId) {
		try {
			Optional<User> userEntity = userRepository.findById(userId);
			if (userEntity.isPresent() && userEntity.orElseThrow().getDeleted() == null) {
				UserInfoOutput userInfoDto = UserInfoOutput.builder()
						.username(userEntity.map(user -> user.getUsername()).orElseThrow())
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

	public boolean updateUserProfile(Integer userId, UpdateUserInput updateUserInput) {
		try {
			Optional<User> userRepo = userRepository.findById(userId);
			if (userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null) {
				User userEntity = userRepo.get();
				userEntity.setUsername(updateUserInput.getUsername());
				userEntity.setEmail(updateUserInput.getEmail());
				userEntity.setPassword(updateUserInput.getPassword());
				userRepository.save(userEntity);
				return true;
			}
			return false;
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex) {
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Boolean deleteUserAccount(Integer userId) {
		try {
			Optional<User> userRepo = userRepository.findById(userId);
			if (userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null) {
				User userEntity = userRepo.orElseThrow();
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

	public boolean sendPasswordReset(ForgotPassEmailInput forgotPassEmailInput) {
		if (userRepository.existsUserByEmail(forgotPassEmailInput.getEmail())) {
			// generate token
			String token = jwtForgotPassToken.generateAccessToken(forgotPassEmailInput.getEmail());
			// TODO: send password reset to email with AWS SNS
			return true;
		}
		return false;
	}

	public boolean updateForgotPassword(ForgotPassTokenInput forgotPassTokenInput) {
		try {
			if (jwtForgotPassToken.validate(forgotPassTokenInput.getToken())) {
				String userEmail = jwtForgotPassToken.getUserEmail(forgotPassTokenInput.getToken());
				Optional<User> userRepo = userRepository.getUserByEmail(userEmail);
				if (userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null) {
					User userEntity = userRepo.get();
					userEntity.setPassword(forgotPassTokenInput.getPassword());
					userRepository.save(userEntity);
					return true;
				}
			}
			return false;
		} catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
				| IllegalArgumentException ex) {
			// TODO: log exception
			throw new ForgotPassTokenException("There is an issue validating the token. ");
		}
	}
}
