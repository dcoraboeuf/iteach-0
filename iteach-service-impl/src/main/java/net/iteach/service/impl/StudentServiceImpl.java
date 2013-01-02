package net.iteach.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CoordinatesService;
import net.iteach.api.StudentService;
import net.iteach.api.model.CoordinatesEntity;
import net.iteach.core.model.Ack;
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

	@Autowired
	public StudentServiceImpl(DataSource dataSource, Validator validator, CoordinatesService coordinatesService) {
		super(dataSource, validator);
		this.coordinatesService = coordinatesService;
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
		// FIXME Check for the associated teacher
		// Total hours
		final BigDecimal studentHours = getStudentHours(id);
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
								coordinatesService.getCoordinates(CoordinatesEntity.STUDENTS, id),
								school,
								studentHours);
					}
					
				});
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getStudentHours(int id) {
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
		// FIXME Check for the associated teacher
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
			coordinatesService.setCoordinates (CoordinatesEntity.STUDENTS, id.getValue(), form.getCoordinates());
		}
		// OK
		return id;
	}
	
	@Override
	@Transactional
	public Ack deleteStudentForTeacher(int teacherId, int id) {
		// Deletes the coordinates
		coordinatesService.removeCoordinates (CoordinatesEntity.STUDENTS, id);
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
		Ack ack = Ack.one(count);
		// Coordinates
		if (ack.isSuccess()) {
			coordinatesService.setCoordinates (CoordinatesEntity.STUDENTS, id, form.getCoordinates());
		}
		// OK
		return ack;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Coordinates getStudentCoordinates(int userId, int id) {
		// FIXME Check for the associated teacher
		return coordinatesService.getCoordinates (CoordinatesEntity.STUDENTS, id);
	}

}
