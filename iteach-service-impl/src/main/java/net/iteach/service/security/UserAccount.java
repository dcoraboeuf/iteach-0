package net.iteach.service.security;

import net.iteach.core.model.AuthenticationMode;
import lombok.Data;

@Data
public class UserAccount {

	private final int id;
	private final AuthenticationMode mode;
	private final String identifier;
	private final String password;
	private final String email;
	private final String firstName;
	private final String lastName;
	private final boolean administrator;
    private final boolean verified;
    private final boolean disabled;

}
