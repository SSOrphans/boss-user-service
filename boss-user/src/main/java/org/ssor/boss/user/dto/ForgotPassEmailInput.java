package org.ssor.boss.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPassEmailInput {

	@Email(message = "Please provide a valid email")
	@NotBlank(message = "Please provide a valid email")
	private String email;
}
