package net.iteach.api.model.copy;

import lombok.Data;
import net.iteach.core.model.Comment;

import java.util.List;

@Data
public abstract class ExportedWithComments {

    private final List<Comment> comments;

}
