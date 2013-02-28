package net.iteach.service.dao.jdbc;

import net.iteach.service.dao.StudentDao;
import net.iteach.service.dao.model.TStudent;
import net.iteach.service.db.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class StudentJdbcDao extends AbstractJdbcDao implements StudentDao {

    private final RowMapper<TStudent> studentRowMapper = new RowMapper<TStudent>() {
        @Override
        public TStudent mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TStudent(
                    rs.getInt("ID"),
                    rs.getString("SUBJECT"),
                    rs.getString("NAME"),
                    rs.getInt("SCHOOL"),
                    rs.getBoolean("DISABLED"));
        }
    };

    @Autowired
    public StudentJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public TStudent getStudentById(int studentId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.STUDENT,
                params("id", studentId),
                studentRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TStudent> findStudentsByTeacher(int teacherId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.STUDENTS_FOR_TEACHER,
                params("teacher", teacherId),
                studentRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TStudent> findStudentsBySchool(int schoolId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.STUDENTS_FOR_SCHOOL,
                params("id", schoolId),
                studentRowMapper
        );
    }
}
