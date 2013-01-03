package net.iteach.service.impl;

import static net.iteach.service.db.SQLUtils.dateToDB;
import static net.iteach.service.db.SQLUtils.timeToDB;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CoordinatesService;
import net.iteach.api.LessonService;
import net.iteach.api.model.CoordinatesEntity;
import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.Lesson;
import net.iteach.core.model.LessonDetails;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.model.StudentLesson;
import net.iteach.core.model.StudentLessons;
import net.iteach.core.model.StudentSummary;
import net.iteach.core.model.StudentSummaryWithCoordinates;
import net.iteach.core.validation.LessonFormValidation;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LessonServiceImpl extends AbstractServiceImpl implements LessonService {
	
	private final CoordinatesService coordinatesService;

	@Autowired
	public LessonServiceImpl(DataSource dataSource, Validator validator, CoordinatesService coordinatesService) {
		super(dataSource, validator);
		this.coordinatesService = coordinatesService;
	}
	
	@Override
	@Transactional(readOnly = true)
	public StudentLessons getLessonsForStudent(int userId, int id, LocalDate date) {
		// FIXME Check for the associated teacher
		// From: first day of the month
		String from = date.withDayOfMonth(1).toString();
		// To: last day of the month
		String to = date.withDayOfMonth(date.dayOfMonth().getMaximumValue()).toString();
		// All lessons
		List<StudentLesson> lessons = getNamedParameterJdbcTemplate().query(
			SQL.LESSONS_FOR_STUDENT,
			params("id", id).addValue("from", from).addValue("to", to),
			new RowMapper<StudentLesson> () {
				@Override
				public StudentLesson mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					return new StudentLesson(
							rs.getInt("id"),
							SQLUtils.dateFromDB(rs.getString("pdate")),
							SQLUtils.timeFromDB(rs.getString("pfrom")),
							SQLUtils.timeFromDB(rs.getString("pto")),
							rs.getString("location")
							);
				}
			}
		);
		// Total hours
		BigDecimal hours = BigDecimal.ZERO;
		for (StudentLesson lesson : lessons) {
			hours = hours.add(getHours(lesson.getFrom(), lesson.getTo()));
		}
		// OK
		return new StudentLessons(
			date,
			lessons,
			hours
		);
	}

	@Override
	@Transactional(readOnly = true)
	public Lessons getLessonsForTeacher(int userId, LessonRange range) {
		// TODO Validation
		return new Lessons(
				getNamedParameterJdbcTemplate().query(
						SQL.LESSONS,
						params("teacher", userId)
							.addValue("from", range.getFrom().toString())
							.addValue("to", range.getTo().toString()),
						new RowMapper<Lesson>() {
							@Override
							public Lesson mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								SchoolSummary school = new SchoolSummary(
										rs.getInt("SCHOOL_ID"),
										rs.getString("SCHOOL_NAME"),
										rs.getString("SCHOOL_COLOR"));
								StudentSummary student = new StudentSummary(
										rs.getInt("STUDENT_ID"),
										rs.getString("STUDENT_SUBJECT"),
										rs.getString("STUDENT_NAME"),
										school);
								return new Lesson(
										rs.getInt("id"),
										student,
										SQLUtils.dateFromDB(rs.getString("pdate")),
										SQLUtils.timeFromDB(rs.getString("pfrom")),
										SQLUtils.timeFromDB(rs.getString("pto")),
										rs.getString("location")
										);
							}
						}));
	}
	
	@Override
	@Transactional(readOnly = true)
	public LessonDetails getLessonDetails(int userId, int id) {
		// FIXME Check for the associated teacher
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.LESSON_DETAILS,
			params("id", id),
			new RowMapper<LessonDetails>() {
				@Override
				public LessonDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					int studentId = rs.getInt("STUDENT_ID");
					SchoolSummary school = new SchoolSummary(
							rs.getInt("SCHOOL_ID"),
							rs.getString("SCHOOL_NAME"),
							rs.getString("SCHOOL_COLOR"));
					StudentSummaryWithCoordinates student = new StudentSummaryWithCoordinates(
							studentId,
							rs.getString("STUDENT_SUBJECT"),
							rs.getString("STUDENT_NAME"),
							school,
							coordinatesService.getCoordinates(CoordinatesEntity.STUDENTS, studentId));
					return new LessonDetails(
							rs.getInt("id"),
							student,
							SQLUtils.dateFromDB(rs.getString("pdate")),
							SQLUtils.timeFromDB(rs.getString("pfrom")),
							SQLUtils.timeFromDB(rs.getString("pto")),
							rs.getString("location")
							);
				}
			}
		);
	}
	
	@Override
	@Transactional
	public ID createLessonForTeacher(int userId, LessonForm form) {
        // Validation
        validate(form, LessonFormValidation.class);
		// FIXME Check for the associated teacher
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int count = getNamedParameterJdbcTemplate().update(
				SQL.LESSON_CREATE,
				params("student", form.getStudent())
					.addValue("date", dateToDB(form.getDate()))
					.addValue("from", timeToDB(form.getFrom()))
					.addValue("to", timeToDB(form.getTo()))
					.addValue("location", form.getLocation()),
				keyHolder);
		return ID.count(count).withId(keyHolder.getKey().intValue());
	}
	
	@Override
	@Transactional
	public Ack editLessonForTeacher(int userId, int id, LessonForm form) {
        // Validation
        validate(form, LessonFormValidation.class);
		// FIXME Check for the associated teacher
		int count = getNamedParameterJdbcTemplate().update(
				SQL.LESSON_UPDATE,
				params("id", id)
					.addValue("student", form.getStudent())
					.addValue("date", dateToDB(form.getDate()))
					.addValue("from", timeToDB(form.getFrom()))
					.addValue("to", timeToDB(form.getTo()))
					.addValue("location", form.getLocation())
				);
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack deleteLessonForTeacher(int teacherId, int id) {
		// FIXME Check for the associated teacher
		int count = getNamedParameterJdbcTemplate().update(SQL.LESSON_DELETE, params("id", id));
		return Ack.one(count);
	}

}
