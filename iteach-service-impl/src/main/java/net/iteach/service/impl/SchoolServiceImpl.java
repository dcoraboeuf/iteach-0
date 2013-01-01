package net.iteach.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.SchoolService;
import net.iteach.api.StudentService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolDetails;
import net.iteach.core.model.SchoolDetailsStudent;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.validation.SchoolFormValidation;
import net.iteach.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolServiceImpl extends AbstractServiceImpl implements
		SchoolService {
	
	private final StudentService studentService;

	@Autowired
	public SchoolServiceImpl(DataSource dataSource, Validator validator, StudentService studentService) {
		super(dataSource, validator);
		this.studentService = studentService;
	}

	@Override
	@Transactional(readOnly = true)
	public SchoolSummaries getSchoolsForTeacher(int teacherId) {
		return new SchoolSummaries(
				getNamedParameterJdbcTemplate().query(SQL.SCHOOLS_FOR_TEACHER, params("teacher", teacherId),
						new RowMapper<SchoolSummary>() {
							@Override
							public SchoolSummary mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								return new SchoolSummary(rs.getInt("id"), rs.getString("name"), rs.getString("color"));
							}
						}));
	}
	
	@Override
	@Transactional(readOnly = true)
	public SchoolDetails getSchoolForTeacher(int userId, int id) {
		// FIXME Check for the associated teacher
		// Student summaries
		final List<SchoolDetailsStudent> students = getNamedParameterJdbcTemplate().query(
			SQL.STUDENTS_FOR_SCHOOL,
			params("id", id),
			new RowMapper<SchoolDetailsStudent> () {

				@Override
				public SchoolDetailsStudent mapRow(ResultSet rs, int rowNum) throws SQLException {
					int studentId = rs.getInt("id");
					return new SchoolDetailsStudent(
						studentId,
						rs.getString("name"),
						rs.getString("subject"),
						studentService.getStudentHours(studentId));
				}
				
			}
		);
		// Details
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.SCHOOL_DETAILS,
			params("id", id),
			new RowMapper<SchoolDetails>() {

				@Override
				public SchoolDetails mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					return new SchoolDetails(rs.getInt("id"), rs.getString("name"), rs.getString("color"), students);
				}
				
			}
		);
	}
	
	@Override
	@Transactional
	public ID createSchoolForTeacher(int teacherId, SchoolForm form) {
        validate(form, SchoolFormValidation.class);
		try {
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			int count = getNamedParameterJdbcTemplate().update(
					SQL.SCHOOL_CREATE,
					params("teacher", teacherId)
						.addValue("name", form.getName())
						.addValue("color", form.getColor()),
					keyHolder);
			return ID.count(count).withId(keyHolder.getKey().intValue());
		} catch (DuplicateKeyException ex) {
			// Duplicate school name
			throw new SchoolNameAlreadyDefined (form.getName());
		}
	}
	
	@Override
	@Transactional
	public Ack deleteSchoolForTeacher(int teacherId, int id) {
		// FIXME Check for the associated teacher
		// TODO Deletes coordinates
		int count = getNamedParameterJdbcTemplate().update(SQL.SCHOOL_DELETE, params("teacher", teacherId).addValue("id", id));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack editSchoolForTeacher(int userId, int id, SchoolForm form) {
		// FIXME Check for the associated teacher
        validate(form, SchoolFormValidation.class);
		try {
			int count = getNamedParameterJdbcTemplate().update(
					SQL.SCHOOL_UPDATE,
					params("teacher", userId)
						.addValue("id", id)
						.addValue("name", form.getName())
						.addValue("color", form.getColor())
					);
			return Ack.one(count);
		} catch (DuplicateKeyException ex) {
			// Duplicate school name
			throw new SchoolNameAlreadyDefined (form.getName());
		}
	}

}
