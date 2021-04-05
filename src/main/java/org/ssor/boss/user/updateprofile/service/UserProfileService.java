/**
 * 
 */
package org.ssor.boss.user.updateprofile.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.updateprofile.dto.UserProfileDto;
import org.ssor.boss.user.updateprofile.entity.UserProfile;
import org.ssor.boss.user.updateprofile.repository.UserProfileDao;

/**
 * @author Christian Angeles
 *
 */
@Service
public class UserProfileService {

	@Autowired
	private UserProfileDao userProfileDao;

	public void updateUserProfile(Integer userId, UserProfileDto userProfileDto) {
		if (userId != null) {
			UserProfile userProfile = convertToUserProfile(userProfileDao.findById(userId));
			
			if(userProfileDto.getDisplayName() != null && !userProfileDto.getDisplayName().isBlank())
				userProfile.setDisplayName(userProfileDto.getDisplayName());
			if(userProfileDto.getEmail() != null && !userProfileDto.getEmail().isBlank())
				userProfile.setEmail(userProfileDto.getEmail());
			if(userProfileDto.getPassword() != null && !userProfileDto.getPassword().isBlank())
				userProfile.setPassword(userProfileDto.getPassword());
			
			userProfileDao.save(userProfile);
		}
	}

	private UserProfile convertToUserProfile(Optional<UserProfile> userProfile) {
		return UserProfile.builder()
				.userId(userProfile.map(user -> user.getUserId()).orElse(null))
				.displayName(userProfile.map(user -> user.getDisplayName()).orElse(null))
				.email(userProfile.map(user -> user.getEmail()).orElse(null))
				.password(userProfile.map(user -> user.getPassword()).orElse(null))
				.created(userProfile.map(user -> user.getCreated()).orElse(null))
				.confirmed(userProfile.map(user -> user.getConfirmed()).orElse(null))
				.build();
	}
}
