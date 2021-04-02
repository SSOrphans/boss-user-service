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
	private Timestamp deleted;
	private Boolean confirmed;
	private Boolean locked;
}
