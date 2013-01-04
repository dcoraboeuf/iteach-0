package net.iteach.api;

import net.iteach.api.model.Entity;
import net.iteach.core.model.Comments;

public interface CommentsService {

	Comments getComments(Entity entity, int id, int offset, int count);

}
