package org.ssor.boss.user.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserProfileDto {

	@Pattern(regexp = "(^[a-zA-Z]{2,} [a-zA-Z]{2,}$)|(^[a-zA-Z]{2,} [a-zA-Z]{2,} [a-zA-Z]{2,}$)", message = "Invalid name.")
	private String displayName;

	@Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$", message = "Invalid email.")
	private String email;

	@Pattern(regexp = "^(([0-9]+[a-fA-F]+)|([a-fA-F]+[0-9]+))+[0-9a-fA-F]*$", message = "Invalid password hash.")
	@Size(min = 64, max = 64, message = "Invalid password hash length.")
	private String password;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfileDto other = (UserProfileDto) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
}
