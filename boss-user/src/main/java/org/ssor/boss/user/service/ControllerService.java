package org.ssor.boss.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssor.boss.core.entity.AccountHolder;
import org.ssor.boss.core.entity.User;
import org.ssor.boss.core.exception.ForgotPassTokenException;
import org.ssor.boss.core.exception.UserDataAccessException;
import org.ssor.boss.core.repository.AccountHolderRepository;
import org.ssor.boss.core.repository.UserRepository;
import org.ssor.boss.core.service.AwsSesService;
import org.ssor.boss.core.service.UserService;
import org.ssor.boss.core.transfer.Email;
import org.ssor.boss.user.dto.ForgotPassEmailInput;
import org.ssor.boss.user.dto.ForgotPassTokenInput;
import org.ssor.boss.user.dto.UpdateProfileInput;
import org.ssor.boss.user.dto.UserInfoOutput;
import org.ssor.boss.user.security.JwtForgotPassToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ControllerService
{

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	JwtForgotPassToken jwtForgotPassToken;

	@Autowired
	PasswordEncoder passwordEncoder;

	//FIXME: Change email
	private final String Sender = "derrian.harris@smoothstack.com";

	public Optional<UserInfoOutput> getUserInfo(Integer userId)
	{
		try
		{
			Optional<User> userEntity = userRepository.findById(userId);
			Optional<AccountHolder> userAccount = accountHolderRepository.findById(userId);
			if (userAccount.isPresent() && userEntity.isPresent() && userEntity.orElseThrow().getDeleted() == null)
			{
				UserInfoOutput userInfoDto = UserInfoOutput.builder()
						.username(userEntity.map(user -> user.getUsername()).orElseThrow())
						.email(userEntity.map(user -> user.getEmail()).orElseThrow())
						.created(LocalDate.ofInstant(Instant.ofEpochMilli(userEntity.map(user -> user.getCreated()).orElseThrow()),
								ZoneId.systemDefault()))
						.fullName(userAccount.map(user -> user.getFullName()).orElseThrow())
						.dob(userAccount.map(user -> user.getDob()).orElseThrow())
						.address(userAccount.map(user -> user.getAddress()).orElseThrow())
						.city(userAccount.map(user -> user.getCity()).orElseThrow())
						.state(userAccount.map(user -> user.getState()).orElseThrow())
						.zip(userAccount.map(user -> user.getZip()).orElseThrow())
						.phone(userAccount.map(user -> user.getPhone()).orElseThrow()).build();
				return Optional.ofNullable(userInfoDto);
			}
			return Optional.empty();
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex)
		{
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public boolean updateUserProfile(Integer userId, UpdateProfileInput updateProfileInput)
	{
		try
		{
			Optional<User> userRepo = userRepository.findById(userId);
			Optional<AccountHolder> accountRepo = accountHolderRepository.findById(userId);
			if (accountRepo.isPresent() && userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null)
			{
				User userEntity = userRepo.get();
				AccountHolder accountHolder = accountRepo.get();

				userEntity.setEmail(updateProfileInput.getEmail());
				accountHolder.setFullName(updateProfileInput.getFullName());
				accountHolder.setAddress(updateProfileInput.getAddress());
				accountHolder.setCity(updateProfileInput.getCity());
				accountHolder.setState(updateProfileInput.getState());
				accountHolder.setZip(updateProfileInput.getZip());
				accountHolder.setPhone(updateProfileInput.getPhone());

				userRepository.save(userEntity);
				accountHolderRepository.save(accountHolder);
				return true;
			}
			return false;
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex)
		{
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Boolean deleteUserAccount(Integer userId)
	{
		try
		{
			Optional<User> userRepo = userRepository.findById(userId);
			if (userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null)
			{
				User userEntity = userRepo.orElseThrow();
				userEntity.setDeleted(Instant.now().toEpochMilli());
				userRepository.save(userEntity);
				return true;
			}
			return false;
		} catch (DataAccessException | IllegalArgumentException | NoSuchElementException ex)
		{
			// TODO: log exception
			throw new UserDataAccessException("There is an issue accessing data. ");
		}
	}

	public Optional<Email> sendPasswordReset(ForgotPassEmailInput forgotPassEmailInput)
	{
		Optional<Email> email = Optional.empty();
		if (userRepository.existsUserByEmail(forgotPassEmailInput.getEmail()))
		{
			// generate token
			String token = jwtForgotPassToken.generateAccessToken(forgotPassEmailInput.getEmail());
			String subject = "Forgot password request";
			String body = "To complete the password reset process, please click here: "
				+ "http://localhost:4200/login/reset-password?token="+token;
			email = Optional.of(new Email(Sender,forgotPassEmailInput.getEmail(),subject,body));
		}
		return email;
	}

	public boolean updateForgotPassword(ForgotPassTokenInput forgotPassTokenInput)
	{
		try
		{
			String password = forgotPassTokenInput.getPassword();
			String token = forgotPassTokenInput.getToken();
			if (jwtForgotPassToken.validate(token)) {
				List<User> userList = userRepository.findAll();
				String userEmail = jwtForgotPassToken.getUserEmail(token);
				Optional<User> userRepo = userRepository.findUserByEmail(userEmail);
				if (userRepo.isPresent() && userRepo.orElseThrow().getDeleted() == null) {
					User userEntity = userRepo.get();
					userEntity.setPassword(passwordEncoder.encode(forgotPassTokenInput.getPassword()));
					userRepository.save(userEntity);
					return true;
				}
			}
			return false;
		} catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
				| IllegalArgumentException ex)
		{
			// TODO: log exception
			throw new ForgotPassTokenException("There is an issue validating the token. ");
		}
	}
}
