/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssor.boss.user.retrieveInfo.dao.UserInfoDao;
import org.ssor.boss.user.retrieveInfo.dto.UserInfoDto;
import org.ssor.boss.user.retrieveInfo.entity.UserInfo;

/**
 * @author Christian Angeles
 *
 */

@Service
public class UserInfoService {

	@Autowired
	UserInfoDao userDao;
	
	public UserInfoDto findUserById(Integer userId) {
		return convertToUserDto(userDao.findById(userId));
	}
	
	private UserInfoDto convertToUserDto(Optional<UserInfo> userInfo) {
		return UserInfoDto.builder()
				.userId(userInfo.map(user -> user.getUserId()).orElse(null))
				.displayName(userInfo.map(user -> user.getDisplayName()).orElse(null))
				.email(userInfo.map(user -> user.getEmail()).orElse(null))
				.created(userInfo.map(user -> user.getCreated()).orElse(null))
				.deleted(userInfo.map(user -> user.getDeleted()).orElse(null))
				.confirmed(userInfo.map(user -> user.getConfirmed()).orElse(null))
				.locked(userInfo.map(user -> user.getLocked()).orElse(null))
				.build();
	}
}
