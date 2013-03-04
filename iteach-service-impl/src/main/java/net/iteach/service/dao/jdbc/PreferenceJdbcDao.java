package net.iteach.service.dao.jdbc;

import net.iteach.service.dao.PreferenceDao;
import net.iteach.service.db.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
public class PreferenceJdbcDao extends AbstractJdbcDao implements PreferenceDao {

    @Autowired
    public PreferenceJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public String getValue(int userId, String name) {
        return getFirstItem(
                SQL.PREF_GET,
                params("user", userId).addValue("name", name),
                String.class);
    }

    @Override
    @Transactional
    public void storeValue(int userId, String name, String value) {
        NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
        // Parameters
        MapSqlParameterSource params = params("user", userId).addValue("name", name);
        // Removes any previous value
        t.update(SQL.PREF_DELETE, params);
        // Stores the value
        t.update(
                SQL.PREF_SET,
                params.addValue("value", value));
    }
}
