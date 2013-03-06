package net.iteach.service.config;

import com.netbeetle.jackson.ObjectMapperFactory;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.security.DefaultSecurityUtils;
import net.iteach.service.support.json.*;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(256);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();

        jsonJoda(mapper);

        return mapper;
    }

    protected void jsonJoda(ObjectMapper mapper) {
        SimpleModule jodaModule = new SimpleModule("JodaTimeModule", new Version(1, 0, 0, null));
        jsonLocalDateTime(jodaModule);
        jsonLocalDate(jodaModule);
        jsonLocalTime(jodaModule);
        mapper.registerModule(jodaModule);
    }

    protected void jsonLocalDateTime(SimpleModule jodaModule) {
        jodaModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        jodaModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
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
