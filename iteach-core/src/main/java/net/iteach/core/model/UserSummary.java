package net.iteach.core.model;

import lombok.Data;

@Data
public class UserSummary {

	private final int id;
	private final String firstName;
	private final String lastName;
	private final String email;

}
