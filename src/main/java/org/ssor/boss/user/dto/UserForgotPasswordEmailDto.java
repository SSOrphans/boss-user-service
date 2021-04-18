package org.ssor.boss.user.dto;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForgotPasswordEmailDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$", message = "Invalid email.")
	private String email;
}
