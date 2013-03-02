package net.iteach.service.dao.jdbc;

import net.iteach.service.dao.ConfigurationDao;
import net.iteach.service.db.SQL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
public class ConfigurationJdbcDao extends AbstractJdbcDao implements ConfigurationDao {

    @Autowired
    public ConfigurationJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public String getValue(String name) {
        return getFirstItem(SQL.CONFIGURATION_GET, params("name", name), String.class);
    }

    @Override
    @Transactional
    public void setValue(String name, String value) {
        // Deletes the property in all cases
        getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_DELETE, params("name", name));
        // Update if not blank
        if (StringUtils.isNotBlank(value)) {
            // Updates the value
            getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_SET, params("name", name).addValue("value", value));
        }
    }
}
