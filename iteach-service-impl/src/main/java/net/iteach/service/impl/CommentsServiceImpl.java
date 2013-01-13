package net.iteach.service.impl;

import static java.lang.String.format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CommentsService;
import net.iteach.api.model.CommentEntity;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.CommentPreview;
import net.iteach.core.model.CommentSummary;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.service.comment.CommentFormatter;
import net.iteach.service.comment.CommentHTMLFormatter;
import net.iteach.service.comment.CommentRawFormatter;
import net.iteach.service.db.SQLUtils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsServiceImpl extends AbstractServiceImpl implements CommentsService {

	private static final String SQL_SELECT_FOR_ENTITY_WITH_ID = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE %s = :entityId AND ID = :commentId";
	private static final String SQL_COUNT_FOR_ENTITY = "SELECT COUNT(ID) FROM COMMENTS WHERE %s = :id";
	private static final String SQL_SELECT_FOR_ENTITY_WITH_OFFSET = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE %s = :id ORDER BY CREATION DESC LIMIT :count OFFSET :offset";
	
	private static final String SQL_CREATION_TIME = "SELECT CREATION FROM COMMENTS WHERE ID = :id";

	private static final String SQL_INSERT = "INSERT INTO COMMENTS (%s, CREATION, EDITION, CONTENT) VALUES (:entityId, :creation, NULL, :content)";

	private static final String SQL_UPDATE = "UPDATE COMMENTS SET EDITION = :edition, CONTENT = :content WHERE ID = :id AND %s = :entityId";

	private static final String SQL_DELETE = "DELETE FROM COMMENTS WHERE ID = :id AND %s = :entityId";
	
	private final EnumMap<CommentFormat, CommentFormatter> commentFormatters;

	@Autowired
	public CommentsServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
		Map<CommentFormat, CommentFormatter> map = new HashMap<>();
		map.put(CommentFormat.RAW, new CommentRawFormatter());
		map.put(CommentFormat.HTML, new CommentHTMLFormatter());
		commentFormatters = new EnumMap<>(map);
	}

	protected String formatComment(String content, CommentFormat format) {
		CommentFormatter formatter = commentFormatters.get(format);
		if (formatter == null) {
			throw new CommentFormatterNotFoundException (format);
		} else {
			return formatter.format (content);
		}
	}
	
	@Override
	public CommentPreview getPreview(CommentPreview request) {
		String content = formatComment (request.getContent(), request.getFormat());
		return request.withContent(content);
	}

	@Override
	@Transactional(readOnly = true)
	public Comments getComments(CommentEntity entity, int id, int offset, int count, final int maxlength, final CommentFormat format) {
		// Total number of comments
		int total = getNamedParameterJdbcTemplate().queryForInt(
			format(SQL_COUNT_FOR_ENTITY, entity.name()),
			params("id", id));
		// Is there more?
		boolean more = (offset + count < total);
		// Gets the list
		return new Comments(
			getNamedParameterJdbcTemplate().query(
				format(SQL_SELECT_FOR_ENTITY_WITH_OFFSET, entity.name()),
				params("id", id).addValue("offset", offset).addValue("count", count),
				new RowMapper<CommentSummary>() {
					@Override
					public CommentSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
						String content = rs.getString("content");
						// Truncate the comment if needed
						boolean summary;
						if (content.length() > maxlength) {
							content = StringUtils.abbreviate(content, maxlength);
							summary = true;
						} else {
							summary = false;
						}
						// Format of the summary
						content = formatComment(content, format);
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
			),
			more
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comment getComment(CommentEntity entity, int id, final int commentId, final CommentFormat format) {
		return getNamedParameterJdbcTemplate().queryForObject(
			format(SQL_SELECT_FOR_ENTITY_WITH_ID, entity.name()),
			params("entityId", id).addValue("commentId", commentId),
			new RowMapper<Comment> () {
				@Override
				public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
					String content = rs.getString("content");
					content = formatComment(content, format);
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
	public Comment editComment(CommentEntity entity, int entityId, CommentFormat format, CommentsForm form) {
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
				throw new CommentUpdateException ();
			} else {
				// Manages the format
				String content = formatComment(form.getContent(), format);
				return new Comment(commentId, creation, edition, format, content);
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
				throw new CommentUpdateException ();
			} else {
				int id = keyHolder.getKey().intValue();
				String content = formatComment(form.getContent(), format);
				return new Comment(id, creation, null, format, content);
			}
		}
	}
	
	@Override
	@Transactional
	public Ack deleteComment(CommentEntity entity, int entityId, int commentId) {
		int count = getNamedParameterJdbcTemplate().update(
			format(SQL_DELETE, entity.name()),
			params("entityId", entityId).addValue("id", commentId));
		return Ack.one(count);
	}

}
