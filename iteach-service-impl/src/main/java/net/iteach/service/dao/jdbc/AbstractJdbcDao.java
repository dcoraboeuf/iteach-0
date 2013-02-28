package net.iteach.service.dao.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractJdbcDao extends NamedParameterJdbcDaoSupport {

    public AbstractJdbcDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    protected <T> T getFirstItem(String sql, MapSqlParameterSource criteria, Class<T> type) {
        List<T> items = getNamedParameterJdbcTemplate().queryForList(sql, criteria, type);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }

    protected MapSqlParameterSource params(String name, Object value) {
        return new MapSqlParameterSource(name, value);
    }
}
