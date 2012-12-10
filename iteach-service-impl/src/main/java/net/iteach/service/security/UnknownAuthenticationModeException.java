package net.iteach.service.security;

import net.iteach.api.model.AuthenticationMode;
import net.iteach.utils.CoreException;

public class UnknownAuthenticationModeException extends CoreException {

	public UnknownAuthenticationModeException(AuthenticationMode mode) {
		super(mode);
	}

}
