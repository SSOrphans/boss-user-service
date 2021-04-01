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
		UserInfoDto userInfoDto = new UserInfoDto();
		
		userInfoDto.setUserId(userInfo.map(user -> user.getUserId()).orElse(null));
		userInfoDto.setDisplayName(userInfo.map(user -> user.getDisplayName()).orElse(null));
		userInfoDto.setEmail(userInfo.map(user -> user.getEmail()).orElse(null));
		userInfoDto.setCreated(userInfo.map(user -> user.getCreated()).orElse(null));
		userInfoDto.setDeleted(userInfo.map(user -> user.getDeleted()).orElse(null));
		userInfoDto.setConfirmed(userInfo.map(user -> user.getConfirmed()).orElse(null));
		userInfoDto.setLocked(userInfo.map(user -> user.getLocked()).orElse(null));
		
		return userInfoDto;
	}
}
