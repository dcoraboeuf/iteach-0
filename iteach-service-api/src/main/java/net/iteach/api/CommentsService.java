package net.iteach.api;

import net.iteach.api.model.CommentEntity;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.CommentPreview;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;

public interface CommentsService {

	Comments getComments(CommentEntity entity, int id, int offset, int count, int maxlength, CommentFormat format);

	Comment editComment(CommentEntity entity, int id, CommentFormat format, CommentsForm form);

	Comment getComment(CommentEntity entity, int id, int commentId, CommentFormat format);

	CommentPreview getPreview(CommentPreview request);

	Ack deleteComment(CommentEntity entity, int entityId, int commentId);

	void removeComments(CommentEntity entity, int id);

}
