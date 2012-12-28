package net.iteach.web.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import net.iteach.web.support.json.LocalDateDeserializer;
import net.iteach.web.support.json.LocalDateSerializer;
import net.iteach.web.support.json.LocalTimeDeserializer;
import net.iteach.web.support.json.LocalTimeSerializer;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
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
		ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
		
		jsonJoda(mapper);
		
		return mapper;
	}

	protected void jsonJoda(ObjectMapper mapper) {
		SimpleModule jodaModule = new SimpleModule("JodaTimeModule", new Version(1, 0, 0, null));
		jsonLocalDate(jodaModule);
		jsonLocalTime(jodaModule);
		mapper.registerModule(jodaModule);
	}

	protected void jsonLocalTime(SimpleModule jodaModule) {
		jodaModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
		jodaModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
	}

	protected void jsonLocalDate(SimpleModule jodaModule) {
		jodaModule.addSerializer(LocalDate.class, new LocalDateSerializer());
		jodaModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
	}
	
}
