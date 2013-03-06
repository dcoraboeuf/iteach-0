package net.iteach.service.dao;

import net.iteach.api.model.CommentEntity;
import net.iteach.core.model.Ack;
import net.iteach.service.dao.model.TComment;
import org.joda.time.DateTime;

import java.util.List;

public interface CommentDao {

    int getTotalNumberForEntity(CommentEntity entity, int id);

    List<TComment> findForEntity(CommentEntity entity, int id, int offset, int count);

    TComment getCommentById(int commentId);

    void editComment(int commentId, String content);

    TComment createComment(CommentEntity entity, int entityId, String content);

    Ack deleteComment(int commentId);

    void importComment(CommentEntity entity, int id, DateTime creation, DateTime edition, String content);
}
