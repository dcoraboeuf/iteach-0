package net.iteach.service.dao.jdbc;

import net.iteach.core.model.Ack;
import net.iteach.core.model.AuthenticationMode;
import net.iteach.service.dao.UserDao;
import net.iteach.service.dao.model.TUser;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.security.UserEmailAlreadyExistsException;
import net.iteach.service.security.UserIdentifierAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
                    rs.getBoolean("verified"),
                    rs.getBoolean("disabled")
            );
        }
    };

    @Autowired
    public UserJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdminInitialized() {
        return getJdbcTemplate().queryForInt(SQL.USER_ADMINISTRATOR_COUNT) > 0;
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
    public TUser findUserByEmail(String email) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.USER_SUMMARY_BY_EMAIL,
                params("email", email),
                userRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public TUser findUserByUsernameForPasswordMode(String username) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    SQL.USER_BY_PASSWORD,
                    params("identifier", username),
                    userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TUser findUserByUsernameForOpenIDMode(String identifier) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    SQL.USER_BY_OPENID,
                    params("identifier", identifier),
                    userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getPassword(int id) {
        return getFirstItem(
                SQL.USER_PASSWORD,
                params("id", id),
                String.class);
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
    public void createUser(AuthenticationMode mode, String identifier, String email, String firstName, String lastName, boolean administrator, boolean verified, String password) {
        // Checks for unicity of identifier
        Integer existingUserId = getFirstItem(SQL.USER_BY_IDENTIFIER, params("identifier", identifier), Integer.class);
        if (existingUserId != null) {
            throw new UserIdentifierAlreadyExistsException(identifier);
        }
        // Checks for unicity of email
        existingUserId = getFirstItem(SQL.USER_ID_BY_EMAIL, params("email", email), Integer.class);
        if (existingUserId != null) {
            throw new UserEmailAlreadyExistsException(email);
        }
        // Parameters
        MapSqlParameterSource params = params("email", email);
        params.addValue("firstName", firstName);
        params.addValue("lastName", lastName);
        // Administrator accounts are not to be verified
        params.addValue("administrator", administrator);
        params.addValue("verified", verified);
        // Mode
        params.addValue("mode", mode.name());
        params.addValue("identifier", identifier);
        params.addValue("password", password);
        // Insert the user
        getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, params);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        getNamedParameterJdbcTemplate().update(SQL.USER_DELETE, params("id", id));
    }

    @Override
    @Transactional
    public Ack userVerified(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.USER_SET_VERIFIED,
                params("id", id)));
    }

    @Override
    @Transactional
    public Ack userDisable(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.USER_DISABLE,
                params("id", id)
        ));
    }

    @Override
    @Transactional
    public Ack userEnable(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.USER_ENABLE,
                params("id", id)
        ));
    }

    @Override
    @Transactional
    public Ack changePassword(int id, String newPassword, String oldPassword) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.USER_CHANGE_PASSWORD,
                params("id", id)
                        .addValue("newpassword", newPassword)
                        .addValue("oldpassword", oldPassword)));
    }
}
