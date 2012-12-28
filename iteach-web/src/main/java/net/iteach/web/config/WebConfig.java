package net.iteach.web.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

import com.netbeetle.jackson.ObjectMapperFactory;

@Configuration
public class WebConfig {
	
	@Bean
	public Strings strings() throws IOException {
		return StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
	}
	
	@Bean
	public Object exporter() throws IOException {
		MBeanExporter exporter = new MBeanExporter();
		exporter.setBeans(Collections.<String,Object>singletonMap("bean:name=strings", strings()));
		return exporter;
	}
	
	@Bean
	public ObjectMapper jacksonObjectMapper() {
		return ObjectMapperFactory.createObjectMapper();
	}
	
}
