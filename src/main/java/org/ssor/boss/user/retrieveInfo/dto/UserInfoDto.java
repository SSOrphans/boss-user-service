/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.dto;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Christian Angeles
 *
 */
@Builder
@Getter
@Setter
public class UserInfoDto {

	private Integer userId;
	private String email;
	private String displayName;
	private Timestamp created;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfoDto other = (UserInfoDto) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
