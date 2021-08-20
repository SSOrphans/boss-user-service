/**
 * 
 */
package org.ssor.boss.user.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ssor.boss.core.entity.Settings;

/**
 * @author Christian Angeles
 *
 */
@Builder
@Getter
public class UserInfoOutput {
	
	private String email;
	private String username;
	private LocalDate created;
	private String fullName;
	private LocalDate dob;
	private String address;
	private String city;
	private String state;
	private Integer zip;
	private String phone;
	private Settings settings;
}
