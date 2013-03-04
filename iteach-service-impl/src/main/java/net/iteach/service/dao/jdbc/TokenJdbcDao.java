package net.iteach.service.dao.jdbc;

import net.iteach.core.model.TokenType;
import net.iteach.service.dao.TokenDao;
import net.iteach.service.dao.model.TToken;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class TokenJdbcDao extends AbstractJdbcDao implements TokenDao {

    @Autowired
    public TokenJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional
    public void saveToken(TokenType type, String key, String token) {
        // Creation date
        Timestamp creation = SQLUtils.toTimestamp(SQLUtils.now());
        // Saves the token
        getNamedParameterJdbcTemplate().update(
                SQL.TOKEN_SAVE,
                new MapSqlParameterSource()
                        .addValue("token", token)
                        .addValue("tokentype", type.name())
                        .addValue("tokenkey", key)
                        .addValue("creation", creation));
    }

    @Override
    @Transactional(readOnly = true)
    public TToken findByTokenAndType(String token, TokenType type) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.TOKEN_BY_TOKEN_AND_TYPE,
                params("token", token).addValue("tokentype", type.name()),
                new RowMapper<TToken>() {

                    @Override
                    public TToken mapRow(ResultSet rs, int index) throws SQLException {
                        return new TToken(
                                rs.getString("token"),
                                SQLUtils.getEnum(TokenType.class, rs, "tokenType"),
                                rs.getString("tokenkey"),
                                SQLUtils.getDateTime(rs, "creation")
                        );
                    }

                });
    }

    @Override
    @Transactional
    public void deleteToken(TokenType type, String key) {
        getNamedParameterJdbcTemplate().update(
                SQL.TOKEN_DELETE,
                new MapSqlParameterSource()
                        .addValue("tokentype", type.name())
                        .addValue("tokenkey", key));
    }

    @Override
    @Transactional
    public int cleanupToken(DateTime cutOffTime) {
        return getNamedParameterJdbcTemplate().update(
                SQL.TOKEN_CLEANUP,
                params("creation", SQLUtils.toTimestamp(cutOffTime)));
    }
}
