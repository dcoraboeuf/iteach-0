package net.iteach.service.token;

import net.iteach.core.model.TokenType;
import net.sf.jstring.support.CoreException;


public class TokenExpiredException extends CoreException {

	public TokenExpiredException(String token, TokenType type, TokenKey key) {
		super(token, type, key);
	}

}
