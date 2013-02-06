package net.iteach.service.token;

import net.iteach.core.model.TokenType;


public class TokenExpiredException extends TokenException {

	public TokenExpiredException(String token, TokenType type, TokenKey key) {
		super(token, type, key);
	}

}
