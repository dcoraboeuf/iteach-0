package net.iteach.service.token;

import net.sf.jstring.support.CoreException;

public abstract class TokenException extends CoreException {

	public TokenException(Object... params) {
		super(params);
	}

}
