package org.ssor.boss.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPassTokenDto {

	@Pattern(regexp = "^(([0-9]+[a-fA-F]+)|([a-fA-F]+[0-9]+))+[0-9a-fA-F]*$", message = "Invalid password hash.")
	@Size(min = 64, max = 64, message = "Invalid password hash length.")
	@NotEmpty(message = "Password cannot be empty or null.")
	private String password;

	@Pattern(regexp = "^(([0-9]+[a-zA-Z-_.]+)|([a-zA-Z]+[0-9-_.]*)|([-_.]+[a-zA-Z0-9]+))+$", message = "Invalid token charset.")
	private String token;
}
