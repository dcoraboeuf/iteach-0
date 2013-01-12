package net.iteach.api;

import org.joda.time.LocalDate;

import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonChange;
import net.iteach.core.model.LessonDetails;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.model.StudentLessons;

public interface LessonService {

	Lessons getLessonsForTeacher(int userId, LessonRange range);

	ID createLessonForTeacher(int userId, LessonForm form);

	Ack editLessonForTeacher(int userId, int id, LessonForm form);

	Ack deleteLessonForTeacher(int userId, int id);

	StudentLessons getLessonsForStudent(int userId, int id, LocalDate date);

	LessonDetails getLessonDetails(int userId, int id);

	Comments getLessonComments(int userId, int lessonId, int offset, int count, int maxlength, CommentFormat format);

	Comment editLessonComment(int userId, int lessonId, CommentFormat format, CommentsForm form);

	Comment getLessonComment(int userId, int lessonId, int commentId, CommentFormat format);

	Ack deleteLessonComment(int userId, int lessonId, int commentId);

	Ack changeLessonForTeacher(int userId, int id, LessonChange change);

}
