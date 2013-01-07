package net.iteach.service.security;

import net.iteach.utils.InputException;

public class UserIdentifierAlreadyExistsException extends InputException {

	public UserIdentifierAlreadyExistsException(String identifier) {
		super(identifier);
	}

}
