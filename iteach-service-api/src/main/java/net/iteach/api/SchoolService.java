package net.iteach.api;

import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolDetails;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;

public interface SchoolService {

	SchoolSummaries getSchoolsForTeacher(int teacherId);

	ID createSchoolForTeacher(int teacherId, SchoolForm form);

	Ack deleteSchoolForTeacher(int teacherId, int id);

	Ack editSchoolForTeacher(int userId, int id, SchoolForm form);

	SchoolDetails getSchoolForTeacher(int userId, int id);

	Coordinates getSchoolCoordinates(int userId, int id);

	Comments getSchoolComments(int userId, int schoolId, int offset, int count, int maxlength, CommentFormat format);

	Comment editSchoolComment(int userId, int schoolId, CommentFormat format, CommentsForm form);

	Comment getSchoolComment(int userId, int schoolId, int commentId, CommentFormat format);

	Ack deleteSchoolComment(int userId, int schoolId, int commentId);

}
