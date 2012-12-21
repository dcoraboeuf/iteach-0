package net.iteach.service.security;

import javax.sql.DataSource;

import org.springframework.security.core.token.Sha512DigestUtils;

import net.iteach.service.impl.AbstractServiceImpl;

public abstract class AbstractSecurityService extends AbstractServiceImpl {

	public AbstractSecurityService(DataSource dataSource) {
		super(dataSource);
	}

	protected String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}

}
