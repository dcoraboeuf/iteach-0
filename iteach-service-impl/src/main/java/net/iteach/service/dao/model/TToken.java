package net.iteach.service.dao.model;

import lombok.Data;
import net.iteach.core.model.TokenType;
import org.joda.time.DateTime;

@Data
public class TToken {

    private final String token;
    private final TokenType type;
    private final String key;
    private final DateTime creation;

}
