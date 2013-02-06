package net.iteach.service.token;

import net.iteach.core.model.TokenType;

public class TokenNotFoundException extends TokenException {

	public TokenNotFoundException(String token, TokenType type) {
		super(token, type);
	}

}
