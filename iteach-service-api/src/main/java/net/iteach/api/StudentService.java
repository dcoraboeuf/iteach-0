package net.iteach.api;

import java.math.BigDecimal;

import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.StudentDetails;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentSummaries;

public interface StudentService {

	StudentSummaries getStudentsForTeacher(int teacherId);

	ID createStudentForTeacher(int teacherId, StudentForm form);

	Ack deleteStudentForTeacher(int teacherId, int id);

	Ack editStudentForTeacher(int userId, int id, StudentForm form);

	StudentDetails getStudentForTeacher(int userId, int id);

	BigDecimal getStudentHours(int userId, int id);

	Coordinates getStudentCoordinates(int userId, int id);

	Comments getStudentComments(int userId, int id, int offset, int count);

	Comment editStudentComment(int userId, int id, CommentsForm form);

}
