package net.iteach.service.dao.jdbc;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static net.iteach.service.db.SQLUtils.dateToDB;
import static net.iteach.service.db.SQLUtils.timeToDB;

@Component
public class LessonJdbcDao extends AbstractJdbcDao implements LessonDao {

    private final RowMapper<TLesson> lessonRowMapper = new RowMapper<TLesson>() {
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
    };

    @Autowired
    public LessonJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional
    public ID createLesson(int student, String location, LocalDate date, LocalTime from, LocalTime to) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int count = getNamedParameterJdbcTemplate().update(
                SQL.LESSON_CREATE,
                params("student", student)
                        .addValue("date", dateToDB(date))
                        .addValue("from", timeToDB(from))
                        .addValue("to", timeToDB(to))
                        .addValue("location", location),
                keyHolder);
        return ID.count(count).withId(keyHolder.getKey().intValue());
    }

    @Override
    @Transactional
    public Ack updateLesson(int id, int student, String location, LocalDate date, LocalTime from, LocalTime to) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.LESSON_UPDATE,
                params("id", id)
                        .addValue("student", student)
                        .addValue("date", dateToDB(date))
                        .addValue("from", timeToDB(from))
                        .addValue("to", timeToDB(to))
                        .addValue("location", location)
        ));
    }

    @Override
    @Transactional
    public Ack deleteLesson(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(SQL.LESSON_DELETE, params("id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public TLesson getLessonById(int id) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.LESSON,
                params("id", id),
                lessonRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TLesson> findLessonsForTeacher(int userId, LocalDateTime from, LocalDateTime to) {
        return getNamedParameterJdbcTemplate().query(
                SQL.LESSONS,
                params("teacher", userId).addValue("from", from.toString()).addValue("to", to.toString()),
                lessonRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getHoursForStudent(int studentId) {
        final AtomicReference<BigDecimal> hours = new AtomicReference<>(BigDecimal.ZERO);
        getNamedParameterJdbcTemplate().query(
                SQL.STUDENT_TOTAL_HOURS,
                params("id", studentId),
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        hours.set(hours.get().add(SQLUtils.getHours(
                                SQLUtils.timeFromDB(rs.getString("pfrom")),
                                SQLUtils.timeFromDB(rs.getString("pto"))
                        )));
                    }
                }
        );
        return hours.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TLesson> findLessonsForStudent(int studentId, LocalDate from, LocalDate to) {
        return getNamedParameterJdbcTemplate().query(
                SQL.LESSONS_FOR_STUDENT,
                params("id", studentId).addValue("from", from.toString()).addValue("to", to.toString()),
                lessonRowMapper
        );
    }
}
