/**
 * 
 */
package org.ssor.boss.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.UserInfoDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.UserInfoServiceException;
import org.ssor.boss.user.repository.UserRepository;

/**
 * @author Christian Angeles
 *
 */

@Service
public class UserInfoService {

	@Autowired
	UserRepository userRepository;

	public UserInfoDto findUserById(Integer userId) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElse(null);
			if(userEntity != null) {
				UserInfoDto userInfoDto = UserInfoDto.builder().userId(userEntity.getId())
						.displayName(userEntity.getDisplayName())
						.email(userEntity.getEmail())
						.created(userEntity.getCreated()).build();
				return userInfoDto;
			}
			return null;
		} catch (DataAccessException daoEx) {
			throw new UserInfoServiceException("There is an issue accessing data. ");
		}
	}
}
