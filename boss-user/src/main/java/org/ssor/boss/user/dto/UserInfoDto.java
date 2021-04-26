/**
 * 
 */
package org.ssor.boss.user.dto;

import java.time.LocalDateTime;

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
	private LocalDateTime created;
}
