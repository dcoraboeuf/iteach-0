package net.iteach.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CommentsService;
import net.iteach.api.CoordinatesService;
import net.iteach.api.SchoolService;
import net.iteach.api.StudentService;
import net.iteach.api.model.CommentEntity;
import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolDetails;
import net.iteach.core.model.SchoolDetailsStudent;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.validation.SchoolFormValidation;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;

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
	private final CoordinatesService coordinatesService;
	private final CommentsService commentsService;

	@Autowired
	public SchoolServiceImpl(DataSource dataSource, Validator validator, StudentService studentService, CoordinatesService coordinatesService, CommentsService commentsService) {
		super(dataSource, validator);
		this.studentService = studentService;
		this.coordinatesService = coordinatesService;
		this.commentsService = commentsService;
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
								return new SchoolSummary(rs.getInt("id"), rs.getString("name"), rs.getString("color"), SQLUtils.moneyFromDB(rs, "hrate"));
							}
						}));
	}
	
	@Override
	@Transactional(readOnly = true)
	public SchoolDetails getSchoolForTeacher(final int userId, final int id) {
		// Check for the associated teacher
		checkTeacherForSchool (userId, id);
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
						studentService.getStudentHours(userId, studentId));
				}
				
			}
		);
		// Total hours
		final AtomicReference<BigDecimal> totalHours = new AtomicReference<>(BigDecimal.ZERO);
		for (SchoolDetailsStudent student : students) {
			totalHours.set(totalHours.get().add(student.getHours()));
		}
		// Details
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.SCHOOL_DETAILS,
			params("id", id),
			new RowMapper<SchoolDetails>() {

				@Override
				public SchoolDetails mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					return new SchoolDetails(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("color"),
						SQLUtils.moneyFromDB(rs, "hrate"),
						coordinatesService.getCoordinates(CoordinateEntity.SCHOOL, id),
						students,
						totalHours.get());
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
			ID id = ID.count(count).withId(keyHolder.getKey().intValue());
			// Coordinates
			if (id.isSuccess()) {
				coordinatesService.setCoordinates (CoordinateEntity.SCHOOL, id.getValue(), form.getCoordinates());
			}
			// OK
			return id;
		} catch (DuplicateKeyException ex) {
			// Duplicate school name
			throw new SchoolNameAlreadyDefined (form.getName());
		}
	}
	
	@Override
	@Transactional
	public Ack deleteSchoolForTeacher(int teacherId, int id) {
		// Check for the associated teacher
		checkTeacherForSchool (teacherId, id);
		// Update
		int count = getNamedParameterJdbcTemplate().update(SQL.SCHOOL_DELETE, params("teacher", teacherId).addValue("id", id));
		// OK
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack editSchoolForTeacher(int userId, int id, SchoolForm form) {
		// Check for the associated teacher
		checkTeacherForSchool (userId, id);
		// Form validation
        validate(form, SchoolFormValidation.class);
        // Query
		try {
			int count = getNamedParameterJdbcTemplate().update(
					SQL.SCHOOL_UPDATE,
					params("teacher", userId)
						.addValue("id", id)
						.addValue("name", form.getName())
						.addValue("color", form.getColor())
					);
			Ack ack = Ack.one(count);
			// Coordinates
			if (ack.isSuccess()) {
				coordinatesService.setCoordinates (CoordinateEntity.SCHOOL, id, form.getCoordinates());
			}
			// OK
			return ack;
		} catch (DuplicateKeyException ex) {
			// Duplicate school name
			throw new SchoolNameAlreadyDefined (form.getName());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Coordinates getSchoolCoordinates(int userId, int id) {
		// Check for the associated teacher
		checkTeacherForSchool (userId, id);
		// OK
		return coordinatesService.getCoordinates (CoordinateEntity.SCHOOL, id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comments getSchoolComments(int userId, int schoolId, int offset, int count, int maxlength, CommentFormat format) {
		// Check for the associated teacher
		checkTeacherForSchool(userId, schoolId);
		// Gets the comments
		return commentsService.getComments (CommentEntity.SCHOOL, schoolId, offset, count, maxlength, format);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comment getSchoolComment(int userId, int schoolId, int commentId, CommentFormat format) {
		// Check for the associated teacher
		checkTeacherForSchool(userId, schoolId);
		// Gets the comment
		return commentsService.getComment (CommentEntity.SCHOOL, schoolId, commentId, format);
	}
	
	@Override
	@Transactional
	public Comment editSchoolComment(int userId, int schoolId, CommentFormat format, CommentsForm form) {
		// Check for the associated teacher
		checkTeacherForSchool(userId, schoolId);
		// Creates the comment
		return commentsService.editComment (CommentEntity.SCHOOL, schoolId, format, form);
	}
	
	@Override
	@Transactional
	public Ack deleteSchoolComment(int userId, int schoolId, int commentId) {
		// Check for the associated teacher
		checkTeacherForSchool(userId, schoolId);
		// Deletes the comment
		return commentsService.deleteComment (CommentEntity.SCHOOL, schoolId, commentId);
	}

}
