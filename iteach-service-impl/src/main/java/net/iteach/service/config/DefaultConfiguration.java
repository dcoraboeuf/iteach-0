package net.iteach.service.config;

import net.iteach.core.security.DefaultSecurityUtils;
import net.iteach.core.security.SecurityUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultConfiguration {
	
	@Bean
	public SecurityUtils securityUtils() {
		return new DefaultSecurityUtils();
	}

}
