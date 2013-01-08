package net.iteach.service.security;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.service.impl.AbstractServiceImpl;

public abstract class AbstractSecurityService extends AbstractServiceImpl {

	public AbstractSecurityService(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

}
