package net.iteach.service.impl;

import static java.lang.String.format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CommentsService;
import net.iteach.api.model.Entity;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.CommentSummary;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.service.db.SQLUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsServiceImpl extends AbstractServiceImpl implements CommentsService {

	private static final String SQL_SELECT_FOR_ENTITY_WITH_ID = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :entityId AND ID = :commentId";
	private static final String SQL_SELECT_FOR_ENTITY_WITH_OFFSET = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id";
	
	private static final String SQL_CREATION_TIME = "SELECT CREATION FROM COMMENTS WHERE ID = :id";

	private static final String SQL_INSERT = "INSERT INTO COMMENTS (ENTITY_TYPE, ENTITY_ID, CREATION, EDITION, CONTENT) VALUES ('%s', :entityId, :creation, NULL, :content)";

	private static final String SQL_UPDATE = "UPDATE COMMENTS SET EDITION = :edition, CONTENT = :content WHERE ID = :id AND ENTITY_TYPE = '%s' AND ENTITY_ID = :entityId";

	@Autowired
	public CommentsServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public Comments getComments(Entity entity, int id, int offset, int count, final int maxlength, final CommentFormat format) {
		// FIXME Offset and count
		return new Comments(
			getNamedParameterJdbcTemplate().query(
				format(SQL_SELECT_FOR_ENTITY_WITH_OFFSET, entity.name()),
				params("id", id),
				new RowMapper<CommentSummary>() {
					@Override
					public CommentSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
						String content = rs.getString("content");
						// FIXME Format of the summary
						// FIXME Truncate the comment if needed
						boolean summary = false;
						return new CommentSummary(
							rs.getInt("id"),
							SQLUtils.getDateTime(rs, "creation"),
							SQLUtils.getDateTime(rs, "edition"),
							format,
							content,
							summary
							);
					}
				}
			)
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comment getComment(Entity entity, int id, final int commentId, final CommentFormat format) {
		return getNamedParameterJdbcTemplate().queryForObject(
			format(SQL_SELECT_FOR_ENTITY_WITH_ID, entity.name()),
			params("entityId", id).addValue("commentId", commentId),
			new RowMapper<Comment> () {
				@Override
				public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
					String content = rs.getString("content");
					// FIXME Formats the content
					return new Comment(
						commentId,
						SQLUtils.getDateTime(rs.getTimestamp("creation")),
						SQLUtils.getDateTime(rs.getTimestamp("edition")),
						format,
						content
					);
				}				
			}
		);
	}
	
	@Override
	@Transactional
	public Comment editComment(Entity entity, int entityId, CommentFormat format, CommentsForm form) {
		final int commentId = form.getId();
		// Edition
		if (commentId > 0) {
			DateTime edition = SQLUtils.now();
			// Gets the creation time
			DateTime creation = SQLUtils.getDateTime(
				getFirstItem(SQL_CREATION_TIME, params("id", commentId), Timestamp.class)
				);
			// Update
			int count = getNamedParameterJdbcTemplate().update(
				format(SQL_UPDATE, entity.name()),
				params("entityId", entityId)
					.addValue("id", commentId)
					.addValue("content", form.getContent())
					.addValue("edition", SQLUtils.toTimestamp(edition)));
			if (count != 1) {
				// TODO Throw another exception
				throw new RuntimeException("Could not update the comment");
			} else {
				// FIXME Manages the format
				return new Comment(commentId, creation, edition, format, form.getContent());
			}
		}
		// Creation
		else {
			DateTime creation = SQLUtils.now();
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			int count = getNamedParameterJdbcTemplate().update(
				format(SQL_INSERT, entity),
				params("entityId", entityId)
					.addValue("content", form.getContent())
					.addValue("creation", SQLUtils.toTimestamp(creation)),
				keyHolder);
			if (count != 1) {
				// TODO Throw another exception
				throw new RuntimeException("Could not insert the comment");
			} else {
				int id = keyHolder.getKey().intValue();
				return new Comment(id, creation, null, format, form.getContent());
			}
		}
	}

}
