package net.iteach.web.config;

import java.io.IOException;
import java.util.Locale;

import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
	
	@Bean
	public Strings strings() throws IOException {
		return StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
	}
	
}
