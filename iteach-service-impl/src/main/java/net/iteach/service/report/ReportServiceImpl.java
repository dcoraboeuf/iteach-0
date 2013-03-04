package net.iteach.service.report;

import net.iteach.api.TeacherService;
import net.iteach.api.report.ReportService;
import net.iteach.core.model.SchoolSummary;
import net.iteach.core.model.StudentSummary;
import net.iteach.core.report.MonthlyReport;
import net.iteach.core.report.SchoolMonthlyHours;
import net.iteach.core.report.StudentMonthlyHours;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.db.SQLUtils;
import org.joda.money.Money;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final TeacherService teacherService;
    private final SecurityUtils securityUtils;
    private final LessonDao lessonDao;

    @Autowired
    public ReportServiceImpl(TeacherService teacherService, SecurityUtils securityUtils, LessonDao lessonDao) {
        this.teacherService = teacherService;
        this.securityUtils = securityUtils;
        this.lessonDao = lessonDao;
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyReport getMonthlyReport(LocalDate date) {
        // Gets the current user
        int userId = securityUtils.getCurrentUserId();
        // From: first day of the month
        LocalDate from = date.withDayOfMonth(1);
        // To: last day of the month
        LocalDate to = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
        // Indexes
        Map<Integer, SchoolMonthlyHours> schoolHoursIndex = new LinkedHashMap<>();
        Map<Integer, StudentMonthlyHours> studentHoursIndex = new LinkedHashMap<>();
        // Gets the full list of lessons
        List<TLesson> lessons = lessonDao.findAllLessonsForTeacher(userId);
        for (TLesson t : lessons) {
            LocalDate lineDate = t.getDate();
            LocalTime timeFrom = t.getFrom();
            LocalTime timeTo = t.getTo();
            BigDecimal hours = SQLUtils.getHours(timeFrom, timeTo);
            boolean monthlyHours = (lineDate.compareTo(from) >= 0) && (lineDate.compareTo(to) <= 0);
            // Gets the student
            int studentId = t.getStudent();
            StudentSummary student = teacherService.getStudentSummary(studentId);
            String studentName = student.getName();
            // School hourly rate
            SchoolSummary school = student.getSchool();
            Money hourlyRate = school.getHourlyRate();
            // Student hours
            StudentMonthlyHours studentHours = studentHoursIndex.get(studentId);
            if (studentHours == null) {
                studentHours = new StudentMonthlyHours(studentId, studentName, student.isDisabled(), hourlyRate);
            }
            studentHours = studentHours.addHours(hours, monthlyHours);
            studentHoursIndex.put(studentId, studentHours);
            // School hours
            int schoolId = school.getId();
            SchoolMonthlyHours schoolHours = schoolHoursIndex.get(schoolId);
            if (schoolHours == null) {
                schoolHours = new SchoolMonthlyHours(schoolId, school.getName(), school.getColor(), hourlyRate);
            }

            schoolHours = schoolHours.addStudent(studentHours);
            schoolHoursIndex.put(schoolId, schoolHours);
        }
        // OK
        return new MonthlyReport(
                new YearMonth(date),
                new ArrayList<>(schoolHoursIndex.values()));
    }

}
