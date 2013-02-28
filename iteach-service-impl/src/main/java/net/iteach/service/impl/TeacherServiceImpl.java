package net.iteach.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.iteach.api.CommentsService;
import net.iteach.api.CoordinatesService;
import net.iteach.api.TeacherService;
import net.iteach.api.model.CommentEntity;
import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.*;
import net.iteach.core.validation.LessonFormValidation;
import net.iteach.core.validation.SchoolFormValidation;
import net.iteach.core.validation.StudentFormValidation;
import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.SchoolDao;
import net.iteach.service.dao.StudentDao;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.dao.model.TSchool;
import net.iteach.service.dao.model.TStudent;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.sf.jstring.LocalizableMessage;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static net.iteach.service.db.SQLUtils.dateToDB;
import static net.iteach.service.db.SQLUtils.timeToDB;

@Service
public class TeacherServiceImpl extends AbstractServiceImpl implements
        TeacherService {

    private final CoordinatesService coordinatesService;
    private final CommentsService commentsService;
    private final LessonDao lessonDao;
    private final StudentDao studentDao;
    private final SchoolDao schoolDao;

    private final Function<TSchool, SchoolSummary> schoolSummaryFunction = new Function<TSchool, SchoolSummary>() {
        @Override
        public SchoolSummary apply(TSchool t) {
            return new SchoolSummary(
                    t.getId(),
                    t.getName(),
                    t.getColor(),
                    t.getHourlyRate()
            );
        }
    };
    private final Function<TStudent, StudentSummary> studentSummaryFunction = new Function<TStudent, StudentSummary>() {
        @Override
        public StudentSummary apply(TStudent t) {
            return new StudentSummary(
                    t.getId(),
                    t.getSubject(),
                    t.getName(),
                    schoolSummaryFunction.apply(schoolDao.getSchoolById(t.getSchool())),
                    t.isDisabled()
            );
        }
    };

    @Autowired
    public TeacherServiceImpl(DataSource dataSource, Validator validator, CoordinatesService coordinatesService, CommentsService commentsService, LessonDao lessonDao, StudentDao studentDao, SchoolDao schoolDao) {
        super(dataSource, validator);
        this.coordinatesService = coordinatesService;
        this.commentsService = commentsService;
        this.lessonDao = lessonDao;
        this.studentDao = studentDao;
        this.schoolDao = schoolDao;
    }

    @Override
    @Transactional(readOnly = true)
    public SchoolSummaries getSchoolsForTeacher(int teacherId) {
        return new SchoolSummaries(
                Lists.transform(
                        schoolDao.findSchoolsByTeacher(teacherId),
                        schoolSummaryFunction)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SchoolDetails getSchoolForTeacher(final int userId, final int id) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, id);

        List<TStudent> studentsForSchool = studentDao.findStudentsBySchool(id);
        final List<SchoolDetailsStudent> students = Lists.transform(
                studentsForSchool,
                new Function<TStudent, SchoolDetailsStudent>() {
                    @Override
                    public SchoolDetailsStudent apply(TStudent t) {
                        return new SchoolDetailsStudent(
                                t.getId(),
                                t.getName(),
                                t.getSubject(),
                                t.isDisabled(),
                                getStudentHours(userId, t.getId())
                        );
                    }
                }
        );
        // Total hours
        final AtomicReference<BigDecimal> totalHours = new AtomicReference<>(BigDecimal.ZERO);
        for (SchoolDetailsStudent student : students) {
            totalHours.set(totalHours.get().add(student.getHours()));
        }
        // Details
        TSchool school = schoolDao.getSchoolById(id);
        return new SchoolDetails(
                school.getId(),
                school.getName(),
                school.getColor(),
                school.getHourlyRate(),
                coordinatesService.getCoordinates(CoordinateEntity.SCHOOL, id),
                students,
                totalHours.get()
        );
    }

    @Override
    @Transactional
    public ID createSchoolForTeacher(int teacherId, SchoolForm form) {
        validate(form, SchoolFormValidation.class);
        ID id = schoolDao.createSchool(teacherId, form.getName(), form.getColor(), form.getHourlyRate());
        // Coordinates
        if (id.isSuccess()) {
            coordinatesService.setCoordinates(CoordinateEntity.SCHOOL, id.getValue(), form.getCoordinates());
        }
        // OK
        return id;
    }

    @Override
    @Transactional
    public Ack deleteSchoolForTeacher(int teacherId, int id) {
        // Check for the associated teacher
        checkTeacherForSchool(teacherId, id);
        // Update
        return schoolDao.deleteSchool(id);
    }

    @Override
    @Transactional
    public Ack editSchoolForTeacher(int userId, int id, SchoolForm form) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, id);
        // Form validation
        validate(form, SchoolFormValidation.class);
        // Query
        Ack ack = schoolDao.updateSchool(id, form.getName(), form.getColor(), form.getHourlyRate());
        // Coordinates
        if (ack.isSuccess()) {
            coordinatesService.setCoordinates(CoordinateEntity.SCHOOL, id, form.getCoordinates());
        }
        // OK
        return ack;
    }

    @Override
    @Transactional(readOnly = true)
    public Coordinates getSchoolCoordinates(int userId, int id) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, id);
        // OK
        return coordinatesService.getCoordinates(CoordinateEntity.SCHOOL, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getSchoolComments(int userId, int schoolId, int offset, int count, int maxlength, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, schoolId);
        // Gets the comments
        return commentsService.getComments(CommentEntity.SCHOOL, schoolId, offset, count, maxlength, format);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getSchoolComment(int userId, int schoolId, int commentId, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, schoolId);
        // Gets the comment
        return commentsService.getComment(CommentEntity.SCHOOL, schoolId, commentId, format);
    }

    @Override
    @Transactional
    public Comment editSchoolComment(int userId, int schoolId, CommentFormat format, CommentsForm form) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, schoolId);
        // Creates the comment
        return commentsService.editComment(CommentEntity.SCHOOL, schoolId, format, form);
    }

    @Override
    @Transactional
    public Ack deleteSchoolComment(int userId, int schoolId, int commentId) {
        // Check for the associated teacher
        checkTeacherForSchool(userId, schoolId);
        // Deletes the comment
        return commentsService.deleteComment(CommentEntity.SCHOOL, schoolId, commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentSummaries getStudentsForTeacher(int teacherId) {
        return new StudentSummaries(
                Lists.transform(
                        studentDao.findStudentsByTeacher(teacherId),
                        studentSummaryFunction
                )
        );
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
                new RowMapper<StudentDetails>() {

                    @Override
                    public StudentDetails mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        SchoolSummary school = new SchoolSummary(
                                rs.getInt("SCHOOL_ID"),
                                rs.getString("SCHOOL_NAME"),
                                rs.getString("SCHOOL_COLOR"),
                                SQLUtils.moneyFromDB(rs, "SCHOOL_HRATE"));
                        return new StudentDetails(
                                rs.getInt("ID"),
                                rs.getString("SUBJECT"),
                                rs.getString("NAME"),
                                coordinatesService.getCoordinates(CoordinateEntity.STUDENT, id),
                                school,
                                studentHours,
                                rs.getBoolean("DISABLED"));
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
            coordinatesService.setCoordinates(CoordinateEntity.STUDENT, id.getValue(), form.getCoordinates());
        }
        // OK
        return id;
    }

    @Override
    @Transactional
    public Ack deleteStudentForTeacher(int teacherId, int id) {
        // Check for the associated teacher
        checkTeacherForStudent(teacherId, id);
        // Deletion
        int count = getNamedParameterJdbcTemplate().update(SQL.STUDENT_DELETE, params("id", id));
        return Ack.one(count);
    }

    @Override
    @Transactional
    public Ack disableStudentForTeacher(int teacherId, int id) {
        // Check for the associated teacher
        checkTeacherForStudent(teacherId, id);
        // Update
        int count = getNamedParameterJdbcTemplate().update(SQL.STUDENT_DISABLE, params("id", id));
        return Ack.one(count);
    }

    @Override
    @Transactional
    public Ack enableStudentForTeacher(int teacherId, int id) {
        // Check for the associated teacher
        checkTeacherForStudent(teacherId, id);
        // Update
        int count = getNamedParameterJdbcTemplate().update(SQL.STUDENT_ENABLE, params("id", id));
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
            coordinatesService.setCoordinates(CoordinateEntity.STUDENT, id, form.getCoordinates());
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
        return coordinatesService.getCoordinates(CoordinateEntity.STUDENT, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getStudentComments(int userId, int studentId, int offset, int count, int maxlength, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForStudent(userId, studentId);
        // Gets the comments
        return commentsService.getComments(CommentEntity.STUDENT, studentId, offset, count, maxlength, format);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getStudentComment(int userId, int studentId, int commentId, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForStudent(userId, studentId);
        // Gets the comment
        return commentsService.getComment(CommentEntity.STUDENT, studentId, commentId, format);
    }

    @Override
    @Transactional
    public Comment editStudentComment(int userId, int studentId, CommentFormat format, CommentsForm form) {
        // Check for the associated teacher
        checkTeacherForStudent(userId, studentId);
        // Creates the comment
        return commentsService.editComment(CommentEntity.STUDENT, studentId, format, form);
    }

    @Override
    @Transactional
    public Ack deleteStudentComment(int userId, int studentId, int commentId) {
        // Check for the associated teacher
        checkTeacherForStudent(userId, studentId);
        // Deletes the comment
        return commentsService.deleteComment(CommentEntity.STUDENT, studentId, commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentLessons getLessonsForStudent(int userId, int id, LocalDate date, Locale locale) {
        // Check for the associated teacher
        checkTeacherForStudent(userId, id);
        // From: first day of the month
        LocalDate from = date.withDayOfMonth(1);
        // To: last day of the month
        LocalDate to = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
        // Localization
        final DateTimeFormatter dateFormat = DateTimeFormat.mediumDate().withLocale(locale);
        final DateTimeFormatter timeFormat = DateTimeFormat.shortTime().withLocale(locale);
        // All lessons
        List<StudentLesson> lessons = Lists.transform(
                lessonDao.findLessonsForStudent(id, from, to),
                new Function<TLesson, StudentLesson>() {

                    @Override
                    public StudentLesson apply(TLesson t) {
                        // Localization
                        String sDate = dateFormat.print(t.getDate());
                        String sFrom = timeFormat.print(t.getFrom());
                        String sTo = timeFormat.print(t.getTo());
                        // OK
                        return new StudentLesson(
                                t.getId(),
                                t.getDate(),
                                t.getFrom(),
                                t.getTo(),
                                t.getLocation(),
                                sDate,
                                sFrom,
                                sTo
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
        return new Lessons(
                Lists.transform(lessonDao.findLessonsForTeacher(userId, range.getFrom(), range.getTo()),
                        new Function<TLesson, Lesson>() {
                            @Override
                            public Lesson apply(TLesson t) {
                                return new Lesson(
                                        t.getId(),
                                        studentSummaryFunction.apply(studentDao.getStudentById(t.getStudent())),
                                        t.getDate(),
                                        t.getFrom(),
                                        t.getTo(),
                                        t.getLocation()
                                );
                            }
                        }
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDetails getLessonDetails(int userId, int id) {
        checkTeacherForLesson(userId, id);
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.LESSON_DETAILS,
                params("id", id),
                new RowMapper<LessonDetails>() {
                    @Override
                    public LessonDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int studentId = rs.getInt("STUDENT_ID");
                        int schoolId = rs.getInt("SCHOOL_ID");
                        SchoolSummaryWithCoordinates school = new SchoolSummaryWithCoordinates(
                                schoolId,
                                rs.getString("SCHOOL_NAME"),
                                rs.getString("SCHOOL_COLOR"),
                                SQLUtils.moneyFromDB(rs, "SCHOOL_HRATE"),
                                coordinatesService.getCoordinates(CoordinateEntity.SCHOOL, schoolId));
                        StudentSummaryWithCoordinates student = new StudentSummaryWithCoordinates(
                                studentId,
                                rs.getString("STUDENT_SUBJECT"),
                                rs.getString("STUDENT_NAME"),
                                school,
                                rs.getBoolean("STUDENT_DISABLED"),
                                coordinatesService.getCoordinates(CoordinateEntity.STUDENT, studentId));
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
        validate(form.getTo().isAfter(form.getFrom()), new LocalizableMessage("lesson.error.timeorder"));
        checkTeacherForStudent(userId, form.getStudent());

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
        checkTeacherForLesson(userId, id);

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
        checkTeacherForLesson(teacherId, id);
        int count = getNamedParameterJdbcTemplate().update(SQL.LESSON_DELETE, params("id", id));
        return Ack.one(count);
    }

    @Override
    @Transactional
    public Ack changeLessonForTeacher(int userId, int lessonId, LessonChange change) {
        // Check for the associated teacher
        checkTeacherForLesson(userId, lessonId);
        // Loads the lesson range
        LessonRange range = getNamedParameterJdbcTemplate().queryForObject(
                SQL.LESSON_RANGE,
                params("id", lessonId),
                new RowMapper<LessonRange>() {
                    @Override
                    public LessonRange mapRow(ResultSet rs, int rowNum) throws SQLException {
                        LocalDate date = SQLUtils.dateFromDB(rs.getString("pdate"));
                        LocalTime from = SQLUtils.timeFromDB(rs.getString("pfrom"));
                        LocalTime to = SQLUtils.timeFromDB(rs.getString("pto"));
                        return new LessonRange(
                                date.toLocalDateTime(from),
                                date.toLocalDateTime(to));
                    }
                });
        // Adjust the range
        LocalDateTime from = range.getFrom();
        LocalDateTime to = range.getTo();
        // Days?
        int dayDelta = change.getDayDelta();
        if (dayDelta != 0) {
            // Shifts both dates
            from = from.plusDays(dayDelta);
            to = to.plusDays(dayDelta);
        }
        // Minutes
        int minuteDelta = change.getMinuteDelta();
        if (minuteDelta != 0) {
            // Shifts only the end
            to = to.plusMinutes(minuteDelta);
        }
        // Redefines the lesson range
        LocalDate pdate = from.toLocalDate();
        LocalTime pfrom = from.toLocalTime();
        LocalTime pto = to.toLocalTime();
        // Updates the period
        int count = getNamedParameterJdbcTemplate().update(
                SQL.LESSON_RANGE_UPDATE,
                params("id", lessonId)
                        .addValue("date", dateToDB(pdate))
                        .addValue("from", timeToDB(pfrom))
                        .addValue("to", timeToDB(pto))
        );
        return Ack.one(count);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getLessonComments(int userId, int lessonId, int offset, int count, int maxlength, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForLesson(userId, lessonId);
        // Gets the comments
        return commentsService.getComments(CommentEntity.LESSON, lessonId, offset, count, maxlength, format);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getLessonComment(int userId, int lessonId, int commentId, CommentFormat format) {
        // Check for the associated teacher
        checkTeacherForLesson(userId, lessonId);
        // Gets the comment
        return commentsService.getComment(CommentEntity.LESSON, lessonId, commentId, format);
    }

    @Override
    @Transactional
    public Comment editLessonComment(int userId, int lessonId, CommentFormat format, CommentsForm form) {
        // Check for the associated teacher
        checkTeacherForLesson(userId, lessonId);
        // Creates the comment
        return commentsService.editComment(CommentEntity.LESSON, lessonId, format, form);
    }

    @Override
    @Transactional
    public Ack deleteLessonComment(int userId, int lessonId, int commentId) {
        // Check for the associated teacher
        checkTeacherForLesson(userId, lessonId);
        // Deletes the comment
        return commentsService.deleteComment(CommentEntity.LESSON, lessonId, commentId);
    }

}
