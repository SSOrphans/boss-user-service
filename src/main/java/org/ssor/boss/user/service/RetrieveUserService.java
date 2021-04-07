/**
 * 
 */
package org.ssor.boss.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.dto.RetrieveUserDto;
import org.ssor.boss.user.entity.UserEntity;
import org.ssor.boss.user.exception.RetrieveUserServiceException;
import org.ssor.boss.user.repository.UserRepository;

/**
 * @author Christian Angeles
 *
 */

@Service
public class RetrieveUserService {

	@Autowired
	UserRepository userRepository;

	public RetrieveUserDto findUserById(Integer userId) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElse(null);
			if(userEntity != null) {
				RetrieveUserDto userInfoDto = RetrieveUserDto.builder().userId(userEntity.getId())
						.displayName(userEntity.getDisplayName())
						.email(userEntity.getEmail())
						.created(userEntity.getCreated()).build();
				return userInfoDto;
			}
			return null;
		} catch (DataAccessException daoEx) {
			throw new RetrieveUserServiceException("There is an issue accessing data. ");
		}
	}
}
