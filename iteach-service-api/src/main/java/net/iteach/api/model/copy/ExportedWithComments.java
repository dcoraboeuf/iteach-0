package net.iteach.api.model.copy;

import lombok.Data;

import java.util.List;

@Data
public abstract class ExportedWithComments {

    private final List<ExportedComment> comments;

}
