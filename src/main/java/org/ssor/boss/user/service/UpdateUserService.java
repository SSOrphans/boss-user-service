/**
 * 
 */
package org.ssor.boss.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.UpdateUserDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.UpdateServiceException;
import org.ssor.boss.user.repository.UserRepository;

/**
 * @author Christian Angeles
 *
 */
@Service
public class UpdateUserService {

	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<String> updateUserProfile(Integer userId, UpdateUserDto userProfileDto) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElse(null);
			if (userEntity != null && userId.equals(userEntity.getId())) {
				if (userProfileDto.getDisplayName() != null && !userProfileDto.getDisplayName().isBlank())
					userEntity.setDisplayName(userProfileDto.getDisplayName());
				if (userProfileDto.getEmail() != null && !userProfileDto.getEmail().isBlank())
					userEntity.setEmail(userProfileDto.getEmail());
				if (userProfileDto.getPassword() != null && !userProfileDto.getPassword().isBlank())
					userEntity.setPassword(userProfileDto.getPassword());
				userRepository.save(userEntity);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Updated user profile.");
			}
			return ResponseEntity.status(HttpStatus.OK).body("User does not exist.");
		} catch (DataAccessException daoEx) {
			throw new UpdateServiceException("There is an issue accessing data. ");
		}
	}
}
