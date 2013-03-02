package net.iteach.service.dao.jdbc;

import net.iteach.core.model.AuthenticationMode;
import net.iteach.service.dao.UserDao;
import net.iteach.service.dao.model.TUser;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserJdbcDao extends AbstractJdbcDao implements UserDao {

    private RowMapper<TUser> userRowMapper = new RowMapper<TUser>() {
        @Override
        public TUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TUser(
                    rs.getInt("id"),
                    SQLUtils.getEnum(AuthenticationMode.class, rs, "mode"),
                    rs.getString("identifier"),
                    rs.getString("email"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getBoolean("administrator"),
                    rs.getBoolean("verified")
            );
        }
    };

    @Autowired
    public UserJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public TUser getUserById(int id) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.USER_BY_ID,
                params("id", id),
                userRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TUser> findAll() {
        return getJdbcTemplate().query(
                SQL.USERS,
                userRowMapper
        );
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        getNamedParameterJdbcTemplate().update(SQL.USER_DELETE, params("id", id));
    }
}
