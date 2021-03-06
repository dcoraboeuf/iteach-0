package net.iteach.service.token;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {

    private final Logger logger = LoggerFactory.getLogger(TokenCleanupTask.class);

    private static final long DELAY = 30L * 24 * 3600 * 1000; // 30 days

    private final TokenService tokenService;

    @Autowired
    public TokenCleanupTask(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Tests for clean-up every hour
     */
    @Scheduled(fixedDelay = DELAY)
    public void cleanupTrigger() {
        logger.info("[token-cleanup] Clean-up trigerring");
        int count = tokenService.cleanup();
        logger.info("[token-cleanup] {} tokens have been deleted", count);
    }

}
