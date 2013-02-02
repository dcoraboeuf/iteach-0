package net.iteach.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.iteach.api.ConfigurationService;
import net.iteach.api.model.ConfigurationKey;
import net.iteach.service.db.SQL;

@Service
public class ConfigurationServiceImpl extends AbstractServiceImpl implements ConfigurationService {

	@Autowired
	public ConfigurationServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public String getConfigurationValue(ConfigurationKey key) {
		String value = getFirstItem(SQL.CONFIGURATION_GET, params("name", key.name()), String.class);
		if (value == null) {
			return key.getDefaultValue();
		} else {
			return value;
		}
	}

	@Override
	@Transactional
	public void setConfigurationValue(ConfigurationKey key, String value) {
		MapSqlParameterSource params = params("name", key.name());
		getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_DELETE, params);
		getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_SET, params.addValue("value", value));
	}

}
