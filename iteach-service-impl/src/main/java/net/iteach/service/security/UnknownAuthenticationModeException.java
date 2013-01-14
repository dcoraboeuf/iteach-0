package net.iteach.service.security;

import net.iteach.core.model.AuthenticationMode;
import net.sf.jstring.support.CoreException;

public class UnknownAuthenticationModeException extends CoreException {

	public UnknownAuthenticationModeException(AuthenticationMode mode) {
		super(mode);
	}

}
