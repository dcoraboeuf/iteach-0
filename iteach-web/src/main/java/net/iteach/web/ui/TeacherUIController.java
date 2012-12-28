package net.iteach.web.ui;

import net.iteach.api.LessonService;
import net.iteach.api.SchoolService;
import net.iteach.api.StudentService;
import net.iteach.core.model.*;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
	@RequestMapping(value = "/lesson", method = RequestMethod.GET)
	public Lessons getLessons(LessonRange range) {
		// Gets the current teacher
		int userId = securityUtils.getCurrentUserId();
		// OK
		return lessonService.getLessonsForTeacher(userId, range);
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
	

}
