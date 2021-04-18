package org.ssor.boss.user.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForgotPasswordTokenDto {

	@Pattern(regexp = "^(([0-9]+[a-fA-F]+)|([a-fA-F]+[0-9]+))+[0-9a-fA-F]*$", message = "Invalid password hash.")
	@Size(min = 64, max = 64, message = "Invalid password hash length.")
	private String password;

	private String token;
}
