package net.iteach.service.token;

import net.iteach.core.model.TokenType;


public class TokenExpiredException extends AbstractTokenException {

	public TokenExpiredException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
