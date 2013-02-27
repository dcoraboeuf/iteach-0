package net.iteach.core.copy;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class Comment {

    private final DateTime creation;
    private final DateTime edition;
    private final String content;

}
