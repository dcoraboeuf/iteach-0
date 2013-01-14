package net.iteach.service.security;

import net.iteach.core.model.TokenType;
import net.sf.jstring.support.CoreException;

public class TokenMatchException extends CoreException {

	public TokenMatchException(TokenType tokenType, String token, String email) {
		super (tokenType, token, email);
	}

}
