package net.iteach.service.config;

import net.iteach.core.security.SecurityUtils;
import net.iteach.service.security.DefaultSecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@Configuration
public class DefaultConfiguration {
	
	@Bean
	public SecurityUtils securityUtils() {
		return new DefaultSecurityUtils();
	}

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
