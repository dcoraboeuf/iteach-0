package net.iteach.web.config;

import java.io.IOException;

import net.iteach.utils.StringsLoader;
import net.sf.jstring.Strings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
	
	@Bean
	public Strings strings() throws IOException {
		return new StringsLoader().load();
	}
	
}
