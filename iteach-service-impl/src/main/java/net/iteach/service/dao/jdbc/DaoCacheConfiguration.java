package net.iteach.service.dao.jdbc;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * Definition of the cache management.
 * <p/>
 * Cache is enabled through {@link ConcurrentMapCacheManager} but this should
 * be replaced by EHCache when load becomes more important on the application.
 */
public class DaoCacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                DaoCacheKeys.SCHOOL,
                DaoCacheKeys.STUDENT
        );
    }

}
