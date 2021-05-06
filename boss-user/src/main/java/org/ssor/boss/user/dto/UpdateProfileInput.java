/**
 * 
 */
package org.ssor.boss.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Christian Angeles
 *
 */
@Getter
@Builder
public class UpdateProfileInput
{
	@Email(message = "Please provide a valid email")
	@NotBlank(message = "Please provide a valid email")
	private String email;

	@Size(max = 64)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Please provide a valid name")
	@NotBlank(message = "Please provide a valid name")
	private String fullName;

	@Size(max = 255)
	@Pattern(regexp = "^[a-zA-Z0-9. ]+$", message = "Please provide a valid address")
	@NotBlank(message = "Please provide a valid address")
	private String address;

	@Size(max = 64)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Please provide a valid city")
	@NotBlank(message = "Please provide a valid city")
	private String city;

	@Size(max = 32)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Please provide a valid state")
	@NotBlank(message = "Please provide a valid state")
	private String state;

	@NotNull(message = "Please provide a valid zip")
	private Integer zip;

	@Size(max = 16)
	@Pattern(regexp = "^[0-9+]+$", message = "Please provide a valid phone")
	@NotBlank(message = "Please provide a valid phone")
	private String phone;
}
