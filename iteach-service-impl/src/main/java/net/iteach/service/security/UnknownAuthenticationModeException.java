package net.iteach.service.security;

import net.iteach.api.model.AuthenticationMode;
import net.sf.jstring.support.CoreException;

public class UnknownAuthenticationModeException extends CoreException {

	public UnknownAuthenticationModeException(AuthenticationMode mode) {
		super(mode);
	}

}
