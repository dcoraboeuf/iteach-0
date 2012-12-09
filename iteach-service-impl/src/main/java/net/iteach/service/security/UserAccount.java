package net.iteach.service.security;

import net.iteach.api.model.AuthenticationMode;
import lombok.Data;

@Data
public class UserAccount {

	private final AuthenticationMode mode;
	private final String identifier;
	private final String email;
	private final String firstName;
	private final String lastName;

}
