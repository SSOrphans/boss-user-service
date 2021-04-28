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
public class UserInfoOutput {
	
	private String email;
	private String username;
	private LocalDateTime created;
}
