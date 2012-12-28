package net.iteach.service.impl;

import static net.iteach.service.db.SQLUtils.dateToDB;
import static net.iteach.service.db.SQLUtils.timeToDB;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.StudentService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentSummaries;
import net.iteach.core.model.StudentSummary;
import net.iteach.core.validation.LessonFormValidation;
import net.iteach.core.validation.StudentFormValidation;
import net.iteach.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl extends AbstractServiceImpl implements
		StudentService {

	@Autowired
	public StudentServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}
	
	@Override
	@Transactional(readOnly = true)
	public StudentSummaries getStudentsForTeacher(int teacherId) {
		return new StudentSummaries(
				getNamedParameterJdbcTemplate().query(SQL.STUDENTS_FOR_TEACHER, params("teacher", teacherId),
						new RowMapper<StudentSummary>() {
							@Override
							public StudentSummary mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								SchoolSummary school = new SchoolSummary(
										rs.getInt("SCHOOL_ID"),
										rs.getString("SCHOOL_NAME"),
										rs.getString("SCHOOL_COLOR"));
								return new StudentSummary(
										rs.getInt("ID"),
										rs.getString("SUBJECT"),
										rs.getString("NAME"),
										school);
							}
						}));
	}
	
	@Override
	@Transactional
	public ID createStudentForTeacher(int teacherId, StudentForm form) {
        // Validation
        validate(form, StudentFormValidation.class);
		// FIXME Check for the associated teacher
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int count = getNamedParameterJdbcTemplate().update(
				SQL.STUDENT_CREATE,
				params("school", form.getSchool())
					.addValue("subject", form.getSubject())
					.addValue("name", form.getName()),
				keyHolder);
		return ID.count(count).withId(keyHolder.getKey().intValue());
	}
	
	@Override
	@Transactional
	public Ack deleteStudentForTeacher(int teacherId, int id) {
		// TODO Deletes coordinates
		// FIXME Check for the associated teacher
		int count = getNamedParameterJdbcTemplate().update(SQL.STUDENT_DELETE, params("id", id));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack editStudentForTeacher(int userId, int id, StudentForm form) {
        // Validation
        validate(form, StudentFormValidation.class);
		// FIXME Check for the associated teacher
		int count = getNamedParameterJdbcTemplate().update(
				SQL.STUDENT_UPDATE,
				params("id", id)
					.addValue("school", form.getSchool())
					.addValue("subject", form.getSubject())
					.addValue("name", form.getName())
				);
		return Ack.one(count);
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

}
