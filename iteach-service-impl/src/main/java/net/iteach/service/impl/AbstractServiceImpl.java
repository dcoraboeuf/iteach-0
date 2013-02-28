package net.iteach.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import net.iteach.core.validation.ValidationException;
import net.iteach.service.db.SQL;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import net.sf.jstring.MultiLocalizable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.security.access.AccessDeniedException;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class AbstractServiceImpl extends NamedParameterJdbcDaoSupport {

    private final Validator validator;
	
	public AbstractServiceImpl(DataSource dataSource, Validator validator) {
		setDataSource(dataSource);
        this.validator = validator;
	}
	
	protected void checkTeacherForSchool(int userId, int id) {
		Integer teacher = getFirstItem(SQL.TEACHER_FOR_SCHOOL, params("school", id).addValue("teacher", userId), Integer.class);
		if (teacher == null) {
			throw new AccessDeniedException(String.format("User %d cannot access school %d", userId, id));
		}
	}

	protected void checkTeacherForStudent(int userId, int id) {
		Integer teacher = getFirstItem(SQL.TEACHER_FOR_STUDENT, params("student", id).addValue("teacher", userId), Integer.class);
		if (teacher == null) {
			throw new AccessDeniedException(String.format("User %d cannot access student %d", userId, id));
		}
	}

	protected void checkTeacherForLesson(int userId, int id) {
		Integer teacher = getFirstItem(SQL.TEACHER_FOR_LESSON, params("lesson", id).addValue("teacher", userId), Integer.class);
		if (teacher == null) {
			throw new AccessDeniedException(String.format("User %d cannot access lesson %d", userId, id));
		}
	}

    /**
     * @deprecated Migration to DAO
     */
    @Deprecated
	protected <T> T getFirstItem (String sql, MapSqlParameterSource criteria, Class<T> type) {
		List<T> items = getNamedParameterJdbcTemplate().queryForList(sql, criteria, type);
		if (items.isEmpty()) {
			return null;
		} else {
			return items.get(0);
		}
	}

    /**
     * @deprecated Migration to DAO
     */
    @Deprecated
	protected MapSqlParameterSource params (String name, Object value) {
		return new MapSqlParameterSource(name, value);
	}
	
	protected void validate (boolean test, Localizable message) {
		if (!test) {
			throw new ValidationException(new MultiLocalizable(Arrays.asList(message)));
		}
	}

    protected void validate (final Object o, Class<?> group) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o, group);
        if (violations != null && !violations.isEmpty()) {
            Collection<Localizable> messages = Collections2.transform(violations, new Function<ConstraintViolation<Object>, Localizable>() {
                @Override
                public Localizable apply(ConstraintViolation<Object> violation) {
                    return getViolationMessage (o, violation);
                }
            });
            // Exception
            throw new ValidationException(new MultiLocalizable(messages));
        }
    }

    protected Localizable getViolationMessage(Object o, ConstraintViolation<Object> violation) {
        // Message code
        String code = String.format("%s.%s",
                violation.getRootBeanClass().getName(),
                violation.getPropertyPath());
        // Message returned by the validator
        Object oMessage;
        String message = violation.getMessage();
        if (StringUtils.startsWith(message, "{net.iteach")) {
            String key = StringUtils.strip(message, "{}");
            oMessage = new LocalizableMessage(key);
        } else {
            oMessage = message;
        }
        // Complete message
        return new LocalizableMessage("validation.field", new LocalizableMessage(code), oMessage);
    }

}
