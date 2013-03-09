package net.iteach.service.dao.jdbc;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.service.dao.StudentDao;
import net.iteach.service.dao.model.TStudent;
import net.iteach.service.db.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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
    public boolean doesStudentBelongToTeacher(int id, int userId) {
        return getFirstItem(SQL.TEACHER_FOR_STUDENT, params("teacher", userId).addValue("id", id), Integer.class) != null;
    }

    @Override
    @Transactional
    public ID createStudent(String name, int school, String subject) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int count = getNamedParameterJdbcTemplate().update(
                SQL.STUDENT_CREATE,
                params("school", school)
                        .addValue("subject", subject)
                        .addValue("name", name),
                keyHolder);
        // Gets the ID
        return ID.count(count).withId(keyHolder.getKey().intValue());
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.STUDENT, key = "#id")
    public Ack deleteStudent(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(SQL.STUDENT_DELETE, params("id", id)));
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.STUDENT, key = "#id")
    public Ack disableStudent(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(SQL.STUDENT_DISABLE, params("id", id)));
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.STUDENT, key = "#id")
    public Ack enableStudent(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(SQL.STUDENT_ENABLE, params("id", id)));
    }

    @Override
    @Transactional
    @CacheEvict(value = DaoCacheKeys.STUDENT, key = "#id")
    public Ack updateStudent(int id, String name, int school, String subject) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.STUDENT_UPDATE,
                params("id", id)
                        .addValue("school", school)
                        .addValue("subject", subject)
                        .addValue("name", name)
        ));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(DaoCacheKeys.STUDENT)
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
