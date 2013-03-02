package net.iteach.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.iteach.api.CommentsService;
import net.iteach.api.model.CommentEntity;
import net.iteach.core.model.*;
import net.iteach.service.comment.CommentFormatter;
import net.iteach.service.comment.CommentHTMLFormatter;
import net.iteach.service.comment.CommentRawFormatter;
import net.iteach.service.dao.CommentDao;
import net.iteach.service.dao.model.TComment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentDao commentDao;
    private final EnumMap<CommentFormat, CommentFormatter> commentFormatters;

    @Autowired
    public CommentsServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
        Map<CommentFormat, CommentFormatter> map = new HashMap<>();
        map.put(CommentFormat.RAW, new CommentRawFormatter());
        map.put(CommentFormat.HTML, new CommentHTMLFormatter());
        commentFormatters = new EnumMap<>(map);
    }

    protected String formatComment(String content, CommentFormat format) {
        CommentFormatter formatter = commentFormatters.get(format);
        if (formatter == null) {
            throw new CommentFormatterNotFoundException(format);
        } else {
            return formatter.format(content);
        }
    }

    @Override
    public CommentPreview getPreview(CommentPreview request) {
        String content = formatComment(request.getContent(), request.getFormat());
        return request.withContent(content);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getComments(CommentEntity entity, int id, int offset, int count, final int maxlength, final CommentFormat format) {
        // Total number of comments
        int total = commentDao.getTotalNumberForEntity(entity, id);
        // Is there more?
        boolean more = (offset + count < total);
        // Gets the list
        return new Comments(
                Lists.transform(
                        commentDao.findForEntity(entity, id, offset, count),
                        new Function<TComment, CommentSummary>() {

                            @Override
                            public CommentSummary apply(TComment t) {
                                String content = t.getContent();
                                // Truncate the comment if needed
                                boolean summary;
                                if (content.length() > maxlength) {
                                    content = StringUtils.abbreviate(content, maxlength);
                                    summary = true;
                                } else {
                                    summary = false;
                                }
                                return new CommentSummary(
                                        t.getId(),
                                        t.getCreation(),
                                        t.getEdition(),
                                        format,
                                        content,
                                        summary
                                );
                            }
                        }
                ),
                more
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(CommentEntity entity, int id, final int commentId, final CommentFormat format) {
        TComment comment = commentDao.getCommentById(commentId);
        return toComment(format, comment);
    }

    private Comment toComment(CommentFormat format, TComment comment) {
        return new Comment(
                comment.getId(),
                comment.getCreation(),
                comment.getEdition(),
                format,
                formatComment(comment.getContent(), format)
        );
    }

    @Override
    @Transactional
    public Comment editComment(CommentEntity entity, int entityId, CommentFormat format, CommentsForm form) {
        final int commentId = form.getId();
        // Edition
        if (commentId > 0) {
            // Edits the comment
            commentDao.editComment(commentId, form.getContent());
            // Manages the format
            return getComment(entity, entityId, commentId, format);
        }
        // Creation
        else {
            return toComment(format, commentDao.createComment(entity, entityId, form.getContent()));
        }
    }

    @Override
    @Transactional
    public Ack deleteComment(CommentEntity entity, int entityId, int commentId) {
        return commentDao.deleteComment(commentId);
    }

}
