package org.ssor.boss.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPassTokenInput {

	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).*$", message = "Password must at least have 1 lower, upper, number, and special character")
	@Size(min = 8, max = 64, message = "Invalid password hash length.")
	@NotBlank(message = "Please provide a valid password")
	private String password;

	@Pattern(regexp = "^(([0-9]+[a-zA-Z-_.]+)|([a-zA-Z]+[0-9-_.]*)|([-_.]+[a-zA-Z0-9]+))+$", message = "Invalid token charset.")
	@NotBlank(message = "Valid token required")
	private String token;
}
