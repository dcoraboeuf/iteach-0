package net.iteach.service.token;

import net.iteach.core.model.TokenType;
import net.sf.jstring.support.CoreException;

public class TokenNotFoundException extends CoreException {

	public TokenNotFoundException(String token, TokenType type) {
		super(token, type);
	}

}
