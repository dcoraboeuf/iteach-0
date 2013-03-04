package net.iteach.service.token;

import net.iteach.core.model.TokenType;
import net.iteach.service.dao.TokenDao;
import net.iteach.service.dao.model.TToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final int RETENTION_DAYS = 30;
    private static final int EXPIRATION_DELAY = 15;

    private final TokenDao tokenDao;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    @Transactional
    public String generateToken(TokenType type, String key) {
        // Generates the token
        String token = createToken(type, key);
        // Saves it
        tokenDao.saveToken(type, key, token);
        // OK
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenKey checkToken(String token, TokenType type) {
        try {
            TToken t = tokenDao.findByTokenAndType(token, type);
            TokenKey key = new TokenKey(t.getKey(), t.getCreation());
            DateTime utcNow = DateTime.now(DateTimeZone.UTC);
            Days days = Days.daysBetween(key.getCreation(), utcNow);
            if (days.isGreaterThan(Days.days(EXPIRATION_DELAY))) {
                throw new TokenExpiredException(token, type, key);
            } else {
                return key;
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new TokenNotFoundException(token, type);
        }
    }

    @Override
    @Transactional
    public void consumesToken(String token, TokenType type, String key) {
        // Checks the token
        checkToken(token, type);
        // Deletes the token
        tokenDao.deleteToken(type, key);
    }

    @Override
    @Transactional
    public int cleanup() {
        // Date cut-off
        DateTime cutOffTime = DateTime.now(DateTimeZone.UTC).minusDays(RETENTION_DAYS);
        // OK
        return tokenDao.cleanupToken(cutOffTime);

    }

    private String createToken(TokenType type, String key) {
        String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
        return Sha512DigestUtils.shaHex(s);
    }

}
