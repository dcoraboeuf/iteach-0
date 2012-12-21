package net.iteach.service.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import javax.sql.DataSource;
import javax.validation.Validator;
import java.util.List;

public abstract class AbstractServiceImpl extends NamedParameterJdbcDaoSupport {

    private final Validator validator;
	
	public AbstractServiceImpl(DataSource dataSource, Validator validator) {
		setDataSource(dataSource);
        this.validator = validator;
	}
	
	protected <T> T getFirstItem (String sql, MapSqlParameterSource criteria, Class<T> type) {
		List<T> items = getNamedParameterJdbcTemplate().queryForList(sql, criteria, type);
		if (items.isEmpty()) {
			return null;
		} else {
			return items.get(0);
		}
	}
	
	protected MapSqlParameterSource params (String name, Object value) {
		return new MapSqlParameterSource(name, value);
	}

}
