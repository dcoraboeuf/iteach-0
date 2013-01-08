package net.iteach.service.token;

import net.iteach.core.model.TokenType;

public class TokenNotFoundException extends AbstractTokenException {

	public TokenNotFoundException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
