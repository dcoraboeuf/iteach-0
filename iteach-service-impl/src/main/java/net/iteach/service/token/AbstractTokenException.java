package net.iteach.service.token;

import net.iteach.core.model.TokenType;
import net.sf.jstring.support.CoreException;

public abstract class AbstractTokenException extends CoreException {

	public AbstractTokenException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
