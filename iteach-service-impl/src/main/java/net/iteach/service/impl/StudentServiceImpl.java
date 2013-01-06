package net.iteach.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CommentsService;
import net.iteach.api.CoordinatesService;
import net.iteach.api.StudentService;
import net.iteach.api.model.Entity;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.model.StudentDetails;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentSummaries;
import net.iteach.core.model.StudentSummary;
import net.iteach.core.validation.StudentFormValidation;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl extends AbstractServiceImpl implements
		StudentService {
	
	private final CoordinatesService coordinatesService;
	private final CommentsService commentsService;

	@Autowired
	public StudentServiceImpl(DataSource dataSource, Validator validator, CoordinatesService coordinatesService, CommentsService commentsService) {
		super(dataSource, validator);
		this.coordinatesService = coordinatesService;
		this.commentsService = commentsService;
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
	@Transactional(readOnly = true)
	public StudentDetails getStudentForTeacher(int userId, final int id) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, id);
		// Total hours
		final BigDecimal studentHours = getStudentHours(userId, id);
		// Details
		return getNamedParameterJdbcTemplate().queryForObject(
				SQL.STUDENT_DETAILS,
				params("id", id),
				new RowMapper<StudentDetails> () {

					@Override
					public StudentDetails mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SchoolSummary school = new SchoolSummary(
								rs.getInt("SCHOOL_ID"),
								rs.getString("SCHOOL_NAME"),
								rs.getString("SCHOOL_COLOR"));
						return new StudentDetails(
								rs.getInt("ID"),
								rs.getString("SUBJECT"),
								rs.getString("NAME"),
								coordinatesService.getCoordinates(Entity.STUDENTS, id),
								school,
								studentHours);
					}
					
				});
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getStudentHours(int userId, int id) {
		checkTeacherForStudent(userId, id);
		final AtomicReference<BigDecimal> hours = new AtomicReference<>(BigDecimal.ZERO);
		getNamedParameterJdbcTemplate().query(
			SQL.STUDENT_TOTAL_HOURS,
			params("id", id),
			new RowCallbackHandler() {
				
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					hours.set(hours.get().add(getHours(
						SQLUtils.timeFromDB(rs.getString("pfrom")),
						SQLUtils.timeFromDB(rs.getString("pto"))
					)));
				}
			}
		);
		final BigDecimal studentHours = hours.get();
		return studentHours;
	}
	
	@Override
	@Transactional
	public ID createStudentForTeacher(int teacherId, StudentForm form) {
        // Validation
        validate(form, StudentFormValidation.class);
		// Check for the associated teacher
		checkTeacherForSchool(teacherId, form.getSchool());
		// Creation
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int count = getNamedParameterJdbcTemplate().update(
				SQL.STUDENT_CREATE,
				params("school", form.getSchool())
					.addValue("subject", form.getSubject())
					.addValue("name", form.getName()),
				keyHolder);
		// Gets the ID
		ID id = ID.count(count).withId(keyHolder.getKey().intValue());
		// Coordinates
		if (id.isSuccess()) {
			coordinatesService.setCoordinates (Entity.STUDENTS, id.getValue(), form.getCoordinates());
		}
		// OK
		return id;
	}
	
	@Override
	@Transactional
	public Ack deleteStudentForTeacher(int teacherId, int id) {
		// Check for the associated teacher
		checkTeacherForStudent(teacherId, id);
		// Deletes the coordinates
		coordinatesService.removeCoordinates (Entity.STUDENTS, id);
		// Deletion
		int count = getNamedParameterJdbcTemplate().update(SQL.STUDENT_DELETE, params("id", id));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack editStudentForTeacher(int userId, int id, StudentForm form) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, id);
        // Validation
        validate(form, StudentFormValidation.class);
        // Update
		int count = getNamedParameterJdbcTemplate().update(
				SQL.STUDENT_UPDATE,
				params("id", id)
					.addValue("school", form.getSchool())
					.addValue("subject", form.getSubject())
					.addValue("name", form.getName())
				);
		Ack ack = Ack.one(count);
		// Coordinates
		if (ack.isSuccess()) {
			coordinatesService.setCoordinates (Entity.STUDENTS, id, form.getCoordinates());
		}
		// OK
		return ack;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Coordinates getStudentCoordinates(int userId, int id) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, id);
		// Gets the coordinates
		return coordinatesService.getCoordinates (Entity.STUDENTS, id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comments getStudentComments(int userId, int studentId, int offset, int count, int maxlength, CommentFormat format) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, studentId);
		// Gets the comments
		return commentsService.getComments (Entity.STUDENTS, studentId, offset, count, maxlength, format);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comment getStudentComment(int userId, int studentId, int commentId, CommentFormat format) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, studentId);
		// Gets the comment
		return commentsService.getComment (Entity.STUDENTS, studentId, commentId, format);
	}
	
	@Override
	@Transactional
	public Comment editStudentComment(int userId, int studentId, CommentFormat format, CommentsForm form) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, studentId);
		// Creates the comment
		return commentsService.editComment (Entity.STUDENTS, studentId, format, form);
	}
	
	@Override
	@Transactional
	public Ack deleteStudentComment(int userId, int studentId, int commentId) {
		// Check for the associated teacher
		checkTeacherForStudent(userId, studentId);
		// Deletes the comment
		return commentsService.deleteComment (Entity.STUDENTS, studentId, commentId);
	}

}
