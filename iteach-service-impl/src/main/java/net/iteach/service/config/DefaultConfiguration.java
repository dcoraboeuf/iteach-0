package net.iteach.service.config;

import javax.validation.Validator;

import net.iteach.core.security.SecurityUtils;
import net.iteach.service.security.DefaultSecurityUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new ShaPasswordEncoder(256);
    }

}
