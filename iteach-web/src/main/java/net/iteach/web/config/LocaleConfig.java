package net.iteach.web.config;

import net.iteach.web.locale.LocaleInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfig {
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleInterceptor();
	}
	
	@Bean
	public LocaleResolver localeResolver () {
		return new CookieLocaleResolver();
	}

}
