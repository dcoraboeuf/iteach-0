package net.iteach.service.dao.jdbc;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.service.dao.SchoolDao;
import net.iteach.service.dao.model.TSchool;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.impl.SchoolNameAlreadyDefined;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SchoolJdbcDao extends AbstractJdbcDao implements SchoolDao {

    private final RowMapper<TSchool> schoolRowMapper = new RowMapper<TSchool>() {
        @Override
        public TSchool mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            return new TSchool(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    SQLUtils.moneyFromDB(rs, "hrate"));
        }
    };

    @Autowired
    public SchoolJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional
    public boolean doesSchoolBelongToTeacher(int id, int userId) {
        return getFirstItem(SQL.TEACHER_FOR_SCHOOL, params("teacher", userId).addValue("id", id), Integer.class) != null;
    }

    @Override
    @Transactional
    public ID createSchool(int teacherId, String name, String color, BigDecimal hourlyRate) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            int count = getNamedParameterJdbcTemplate().update(
                    SQL.SCHOOL_CREATE,
                    params("teacher", teacherId)
                            .addValue("name", name)
                            .addValue("color", color)
                            .addValue("hourlyRate", hourlyRate),
                    keyHolder);
            return ID.count(count).withId(keyHolder.getKey().intValue());
        } catch (DuplicateKeyException ex) {
            // Duplicate school name
            throw new SchoolNameAlreadyDefined(name);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.SCHOOL, key = "#id")
    public Ack deleteSchool(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(SQL.SCHOOL_DELETE, params("id", id)));
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.SCHOOL, key = "#id")
    public Ack updateSchool(int id, String name, String color, BigDecimal hourlyRate) {
        try {
            return Ack.one(getNamedParameterJdbcTemplate().update(
                    SQL.SCHOOL_UPDATE,
                    params("id", id)
                            .addValue("name", name)
                            .addValue("color", color)
                            .addValue("hourlyRate", hourlyRate)
            ));
        } catch (DuplicateKeyException ex) {
            // Duplicate school name
            throw new SchoolNameAlreadyDefined(name);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(DaoCacheKeys.SCHOOL)
    public TSchool getSchoolById(int id) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.SCHOOL,
                params("id", id),
                schoolRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TSchool> findSchoolsByTeacher(int teacherId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.SCHOOLS_FOR_TEACHER,
                params("teacher", teacherId),
                schoolRowMapper
        );
    }
}
