/**
 * 
 */
package org.ssor.boss.user.updateprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UserProfileDto {

	@JsonProperty
	private String displayName;

	@JsonProperty
	private String email;

	@JsonProperty
	private String password;
}
