package net.iteach.service.dao.jdbc;

import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LessonJdbcDao extends AbstractJdbcDao implements LessonDao {

    @Autowired
    public LessonJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TLesson> findLessonsForStudent(int studentId, LocalDate from, LocalDate to) {
        return getNamedParameterJdbcTemplate().query(
                SQL.LESSONS_FOR_STUDENT,
                params("id", studentId).addValue("from", from.toString()).addValue("to", to.toString()),
                new RowMapper<TLesson>() {
                    @Override
                    public TLesson mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        LocalDate rsDate = SQLUtils.dateFromDB(rs.getString("pdate"));
                        LocalTime rsFrom = SQLUtils.timeFromDB(rs.getString("pfrom"));
                        LocalTime rsTo = SQLUtils.timeFromDB(rs.getString("pto"));
                        return new TLesson(
                                rs.getInt("id"),
                                rs.getInt("student"),
                                rsDate,
                                rsFrom,
                                rsTo,
                                rs.getString("location"));
                    }
                }
        );
    }
}
