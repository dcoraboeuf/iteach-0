package net.iteach.web.ui;

import net.iteach.api.LessonService;
import net.iteach.api.SchoolService;
import net.iteach.api.StudentService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonDetails;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.model.SchoolDetails;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.model.StudentDetails;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentLessons;
import net.iteach.core.model.StudentSummaries;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui/teacher")
public class TeacherUIController extends AbstractUIController implements TeacherUI {

	private final SchoolService schoolService;
	private final StudentService studentService;
	private final LessonService lessonService;

	@Autowired
	public TeacherUIController(SchoolService schoolService,
			StudentService studentService,
			LessonService lessonService,
			SecurityUtils securityUtils,
			ErrorHandler errorHandler,
			Strings strings) {
        super(securityUtils, errorHandler, strings);
		this.schoolService = schoolService;
		this.studentService = studentService;
		this.lessonService = lessonService;
	}

	@Override
	@RequestMapping(value = "/school", method = RequestMethod.GET)
	public @ResponseBody
	SchoolSummaries getSchools() {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.getSchoolsForTeacher(userId);
	}
	
	@Override
	@RequestMapping(value = "/school/{id}", method = RequestMethod.GET)
	public @ResponseBody SchoolDetails getSchool(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.getSchoolForTeacher (userId, id);
	}

	@Override
	@RequestMapping(value = "/school", method = RequestMethod.POST)
	public @ResponseBody
	ID createSchool(@RequestBody SchoolForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.createSchoolForTeacher(userId, form);
	}

	@Override
	@RequestMapping(value = "/school/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	Ack deleteSchool(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.deleteSchoolForTeacher(userId, id);
	}

	@Override
	@RequestMapping(value = "/school/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	Ack editSchool(@PathVariable int id, @RequestBody SchoolForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.editSchoolForTeacher(userId, id, form);
	}

	@Override
	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public @ResponseBody StudentSummaries getStudents() {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.getStudentsForTeacher(userId);
	}

	@Override
	@RequestMapping(value = "/student", method = RequestMethod.POST)
	public @ResponseBody ID createStudent(@RequestBody StudentForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.createStudentForTeacher(userId, form);
	}

	@Override
	@RequestMapping(value = "/student/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Ack deleteStudent(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.deleteStudentForTeacher(userId, id);
	}

	@Override
	@RequestMapping(value = "/student/{id}", method = RequestMethod.PUT)
	public @ResponseBody Ack editStudent(@PathVariable int id, @RequestBody StudentForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.editStudentForTeacher(userId, id, form);
	}
	
	@Override
	@RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
	public @ResponseBody StudentDetails getStudent(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.getStudentForTeacher (userId, id);
	}
	
	@Override
	@RequestMapping(value = "/student/{id}/lessons/{date}", method = RequestMethod.GET)
	public @ResponseBody StudentLessons getStudentLessons(@PathVariable int id, @PathVariable LocalDate date) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonsForStudent (userId, id, date);
	}

	@Override
	@RequestMapping(value = "/lesson", method = RequestMethod.GET)
	public @ResponseBody Lessons getLessons(@RequestBody LessonRange range) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonsForTeacher(userId, range);
	}
	
	@Override
	@RequestMapping(value = "/lesson/{id:\\d+}", method = RequestMethod.GET)
	public @ResponseBody LessonDetails getLesson(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonDetails(userId, id);
	}

	@Override
	@RequestMapping(value = "/lesson", method = RequestMethod.POST)
	public @ResponseBody ID createLesson(@RequestBody LessonForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.createLessonForTeacher(userId, form);
	}

	@Override
	@RequestMapping(value = "/lesson/{id}", method = RequestMethod.PUT)
	public @ResponseBody Ack editLesson(@PathVariable int id, @RequestBody LessonForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.editLessonForTeacher(userId, id, form);
	}

	@Override
	@RequestMapping(value = "/lesson/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Ack deleteLesson(@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.deleteLessonForTeacher(userId, id);
	}

	@Override
	@RequestMapping(value = "/student/{id:\\d+}/coordinates", method = RequestMethod.GET)
	public @ResponseBody Coordinates getStudentCoordinates (@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.getStudentCoordinates (userId, id);
	}

	@Override
	@RequestMapping(value = "/school/{id:\\d+}/coordinates", method = RequestMethod.GET)
	public @ResponseBody Coordinates getSchoolCoordinates (@PathVariable int id) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.getSchoolCoordinates (userId, id);
	}
	
	@Override
	@RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
	public @ResponseBody Comments getLessonComments(@PathVariable int lessonId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonComments(userId, lessonId, offset, count, maxlength, format);
	}
	
	@Override
	@RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
	public @ResponseBody Comment getLessonComment(@PathVariable int lessonId, @PathVariable int commentId, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonComment(userId, lessonId, commentId, format);
	}
	
	@Override
	@RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
	public @ResponseBody Comment editLessonComment(@PathVariable int lessonId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.editLessonComment(userId, lessonId, format, form);
	}
	
	@Override
	@RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
	public @ResponseBody Ack deleteLessonComment(@PathVariable int lessonId, @PathVariable int commentId) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.deleteLessonComment(userId, lessonId, commentId);
	}
	
	@Override
	@RequestMapping(value = "/school/{schoolId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
	public @ResponseBody Comments getSchoolComments(@PathVariable int schoolId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.getSchoolComments(userId, schoolId, offset, count, maxlength, format);
	}
	
	@Override
	@RequestMapping(value = "/school/{schoolId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
	public @ResponseBody Comment getSchoolComment(@PathVariable int schoolId, @PathVariable int commentId, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.getSchoolComment(userId, schoolId, commentId, format);
	}
	
	@Override
	@RequestMapping(value = "/school/{schoolId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
	public @ResponseBody Comment editSchoolComment(@PathVariable int schoolId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.editSchoolComment(userId, schoolId, format, form);
	}
	
	@Override
	@RequestMapping(value = "/school/{schoolId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
	public @ResponseBody Ack deleteSchoolComment(@PathVariable int schoolId, @PathVariable int commentId) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return schoolService.deleteSchoolComment(userId, schoolId, commentId);
	}
	
	@Override
	@RequestMapping(value = "/student/{studentId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
	public @ResponseBody Comments getStudentComments(@PathVariable int studentId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.getStudentComments(userId, studentId, offset, count, maxlength, format);
	}
	
	@Override
	@RequestMapping(value = "/student/{studentId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
	public @ResponseBody Comment getStudentComment(@PathVariable int studentId, @PathVariable int commentId, @PathVariable CommentFormat format) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.getStudentComment(userId, studentId, commentId, format);
	}
	
	@Override
	@RequestMapping(value = "/student/{studentId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
	public @ResponseBody Comment editStudentComment(@PathVariable int studentId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.editStudentComment(userId, studentId, format, form);
	}
	
	@Override
	@RequestMapping(value = "/student/{studentId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
	public @ResponseBody Ack deleteStudentComment(@PathVariable int studentId, @PathVariable int commentId) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return studentService.deleteStudentComment(userId, studentId, commentId);
	}

}
