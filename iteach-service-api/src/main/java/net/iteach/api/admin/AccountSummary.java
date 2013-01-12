package net.iteach.api.admin;

import net.iteach.api.model.AuthenticationMode;
import lombok.Data;

@Data
public class AccountSummary {

	private final boolean self;
	private final int id;
	private final AuthenticationMode mode;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final boolean admin;
	private final boolean verified;

}
