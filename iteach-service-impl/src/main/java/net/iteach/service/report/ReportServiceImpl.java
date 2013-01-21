package net.iteach.service.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.report.ReportService;
import net.iteach.core.report.MonthlyReport;
import net.iteach.core.report.SchoolHours;
import net.iteach.core.report.StudentHours;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.impl.AbstractServiceImpl;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl extends AbstractServiceImpl implements ReportService {

	private final SecurityUtils securityUtils;

	@Autowired
	public ReportServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils) {
		super(dataSource, validator);
		this.securityUtils = securityUtils;
	}

	@Override
	@Transactional(readOnly = true)
	public MonthlyReport getMonthlyReport(LocalDate date) {
		// Gets the current user
		int userId = securityUtils.getCurrentUserId();
		// From: first day of the month
		String from = date.withDayOfMonth(1).toString();
		// To: last day of the month
		String to = date.withDayOfMonth(date.dayOfMonth().getMaximumValue()).toString();
		// Indexes
		final Map<Integer, SchoolHours> schoolHoursIndex = new LinkedHashMap<>();
		final Map<Integer, StudentHours> studentHoursIndex = new LinkedHashMap<>();
		// Gets the full list of lessons
		getNamedParameterJdbcTemplate().query(
			SQL.REPORT_MONTHLY,
			params("teacher", userId).addValue("from", from).addValue("to", to),
			new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					LocalTime timeFrom = SQLUtils.timeFromDB(rs.getString("pfrom"));
					LocalTime timeTo = SQLUtils.timeFromDB(rs.getString("pto"));
					BigDecimal hours = getHours(timeFrom, timeTo);
	
					int studentId = rs.getInt("student");
					String studentName = rs.getString("student_name");
					StudentHours studentHours = studentHoursIndex.get(studentId);
					if (studentHours == null) {
						studentHours = new StudentHours(studentId, studentName, hours);
					} else {
						studentHours = studentHours.addHours(hours);
					}
					studentHoursIndex.put(studentId, studentHours);
	
					int schoolId = rs.getInt("school");
					String schoolName = rs.getString("school_name");
					String schoolColor = rs.getString("school_color");
					SchoolHours schoolHours = schoolHoursIndex.get(schoolId);
					if (schoolHours == null) {
						schoolHours = new SchoolHours(schoolId, schoolName, schoolColor);
					}
					
					schoolHours = schoolHours.addStudent(studentHours);
					schoolHoursIndex.put(schoolId, schoolHours);
				}
		});
		// OK
		return new MonthlyReport(
			new YearMonth(date),
			new ArrayList<>(schoolHoursIndex.values()));
	}

}
