package net.iteach.service.dao;

import net.iteach.core.model.TokenType;
import net.iteach.service.dao.model.TToken;
import org.joda.time.DateTime;

public interface TokenDao {

    TToken findByTokenAndType(String token, TokenType type);

    void saveToken(TokenType type, String key, String token);

    void deleteToken(TokenType type, String key);

    int cleanupToken(DateTime cutOffTime);
}
