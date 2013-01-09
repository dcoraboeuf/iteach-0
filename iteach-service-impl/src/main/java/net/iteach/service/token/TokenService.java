package net.iteach.service.token;

import net.iteach.core.model.TokenType;

public interface TokenService {

	String generateToken(TokenType type, String key);

	TokenKey checkToken(String token, TokenType type);

	void consumesToken(String token, TokenType type, String key);

	int cleanup();

}
