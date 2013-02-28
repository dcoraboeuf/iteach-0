package net.iteach.service.dao.jdbc;

import net.iteach.service.dao.SchoolDao;
import net.iteach.service.dao.model.TSchool;
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
    @Transactional(readOnly = true)
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