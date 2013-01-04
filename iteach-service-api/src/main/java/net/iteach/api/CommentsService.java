package net.iteach.api;

import net.iteach.api.model.Entity;
import net.iteach.core.model.Comment;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;

public interface CommentsService {

	Comments getComments(Entity entity, int id, int offset, int count);

	Comment editComment(Entity entity, int id, CommentsForm form);

}
