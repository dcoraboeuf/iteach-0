package net.iteach.api.model.copy;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class ExportedComment {

    private final DateTime creation;
    private final DateTime edition;
    private final String content;
}
