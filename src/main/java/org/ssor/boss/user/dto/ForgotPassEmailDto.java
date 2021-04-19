package org.ssor.boss.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPassEmailDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$", message = "Invalid email.")
	@NotEmpty(message = "Email cannot be empty or null.")
	private String email;
}
