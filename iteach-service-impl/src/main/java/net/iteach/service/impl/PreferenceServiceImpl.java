package net.iteach.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import net.iteach.api.PreferenceService;
import net.iteach.core.model.PreferenceKey;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreferenceServiceImpl extends AbstractServiceImpl implements PreferenceService {

    private final SecurityUtils securityUtils;

	@Autowired
	public PreferenceServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils) {
		super(dataSource, validator);
        this.securityUtils = securityUtils;
    }

	@Override
    @Transactional(readOnly = true)
	public String getPreference(PreferenceKey key) {
        // Gets the current user
        int userId = securityUtils.getCurrentUserId();
        // Gets the stored value
        String value = getFirstItem(SQL.PREF_GET, params("user", userId).addValue("name", key.name()), String.class);
        // If not found, returns the default value
        if (value == null) {
            return key.getDefaultValue();
        } else {
            return value;
        }
	}

	@Override
    @Transactional
	public void setPreference(PreferenceKey key, String value) {
        NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
        // Gets the current user
        int userId = securityUtils.getCurrentUserId();
        // Parameters
        MapSqlParameterSource params = params("user", userId).addValue("name", key.name());
        // Removes any previous value
        t.update(SQL.PREF_DELETE, params);
        // In any case, trims the value
        String valueToStore = StringUtils.trim(value);
        // Validates and format the value
        valueToStore = key.validateAndFormat(value);
        // Stores the value
        t.update(
                SQL.PREF_SET,
                params.addValue("value", valueToStore));
	}
	
	

}
