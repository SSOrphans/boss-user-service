package org.ssor.boss.user.dto;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailDto {

	@Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$", message = "Invalid email.")
	private String email;
}
