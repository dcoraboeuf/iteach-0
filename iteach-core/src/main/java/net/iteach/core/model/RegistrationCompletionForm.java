package net.iteach.core.model;

import lombok.Data;

@Data
public class RegistrationCompletionForm {

	private final String token;
	private final UserSummary user;

}
