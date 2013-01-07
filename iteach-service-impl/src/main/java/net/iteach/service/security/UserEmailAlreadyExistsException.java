package net.iteach.service.security;

import net.iteach.utils.InputException;

public class UserEmailAlreadyExistsException extends InputException {

	public UserEmailAlreadyExistsException(String email) {
		super(email);
	}

}
