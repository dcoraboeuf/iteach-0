package net.iteach.service.dao.jdbc;

import net.iteach.api.model.CommentEntity;
import net.iteach.core.model.Ack;
import net.iteach.service.dao.CommentDao;
import net.iteach.service.dao.model.TComment;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.impl.CommentUpdateException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

@Component
public class CommentJdbcDao extends AbstractJdbcDao implements CommentDao {

    private static final String SQL_SELECT_WITH_ID = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE ID = :commentId";
    private static final String SQL_COUNT_FOR_ENTITY = "SELECT COUNT(ID) FROM COMMENTS WHERE %s = :id";
    private static final String SQL_SELECT_FOR_ENTITY_WITH_OFFSET = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE %s = :id ORDER BY ID DESC LIMIT :count OFFSET :offset";
    private static final String SQL_INSERT = "INSERT INTO COMMENTS (%s, CREATION, EDITION, CONTENT) VALUES (:entityId, :creation, NULL, :content)";
    private static final String SQL_IMPORT = "INSERT INTO COMMENTS (%s, CREATION, EDITION, CONTENT) VALUES (:entityId, :creation, :edition, :content)";
    private static final String SQL_EDIT = "UPDATE COMMENTS SET EDITION = :edition, CONTENT = :content WHERE ID = :id";
    private static final String SQL_DELETE = "DELETE FROM COMMENTS WHERE ID = :id";

    private final RowMapper<TComment> commentRowMapper =
            new RowMapper<TComment>() {
                @Override
                public TComment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new TComment(
                            rs.getInt("id"),
                            SQLUtils.getDateTime(rs, "creation"),
                            SQLUtils.getDateTime(rs, "edition"),
                            rs.getString("content")
                    );
                }
            };

    @Autowired
    public CommentJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalNumberForEntity(CommentEntity entity, int id) {
        return getNamedParameterJdbcTemplate().queryForInt(
                format(SQL_COUNT_FOR_ENTITY, entity.name()),
                params("id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TComment> findForEntity(CommentEntity entity, int id, int offset, int count) {
        return getNamedParameterJdbcTemplate().query(
                format(SQL_SELECT_FOR_ENTITY_WITH_OFFSET, entity.name()),
                params("id", id).addValue("offset", offset).addValue("count", count),
                commentRowMapper
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TComment getCommentById(int commentId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL_SELECT_WITH_ID,
                params("commentId", commentId),
                commentRowMapper
        );
    }

    @Override
    @Transactional
    public void editComment(int commentId, String content) {
        // Update
        int count = getNamedParameterJdbcTemplate().update(
                SQL_EDIT,
                params("id", commentId)
                        .addValue("content", content)
                        .addValue("edition", SQLUtils.toTimestamp(SQLUtils.now())));
        if (count != 1) {
            throw new CommentUpdateException();
        }
    }

    @Override
    @Transactional
    public TComment createComment(CommentEntity entity, int entityId, String content) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int count = getNamedParameterJdbcTemplate().update(
                format(SQL_INSERT, entity),
                params("entityId", entityId)
                        .addValue("content", content)
                        .addValue("creation", SQLUtils.toTimestamp(SQLUtils.now())),
                keyHolder);
        if (count != 1) {
            throw new CommentUpdateException();
        } else {
            return getCommentById(keyHolder.getKey().intValue());
        }
    }

    @Override
    @Transactional
    public void importComment(CommentEntity entity, int id, DateTime creation, DateTime edition, String content) {
        getNamedParameterJdbcTemplate().update(
                format(SQL_IMPORT, entity),
                params("entityId", id)
                        .addValue("content", content)
                        .addValue("creation", SQLUtils.toTimestamp(creation))
                        .addValue("edition", SQLUtils.toTimestamp(edition)));
    }

    @Override
    @Transactional
    public Ack deleteComment(int commentId) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL_DELETE,
                params("id", commentId)));
    }
}
