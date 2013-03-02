package net.iteach.service.dao.model;

import lombok.Data;
import net.iteach.api.model.CommentEntity;
import org.joda.time.DateTime;

@Data
public class TComment {

    private final int id;
    private final DateTime creation;
    private final DateTime edition;
    private final String content;

}
