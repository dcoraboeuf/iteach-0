package net.iteach.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import net.iteach.core.validation.ValidationException;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import net.sf.jstring.MultiLocalizable;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractServiceImpl {

    private final Validator validator;

    public AbstractServiceImpl(Validator validator) {
        this.validator = validator;
    }

    protected void validate(boolean test, Localizable message) {
        if (!test) {
            throw new ValidationException(new MultiLocalizable(Arrays.asList(message)));
        }
    }

    protected void validate(final Object o, Class<?> group) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o, group);
        if (violations != null && !violations.isEmpty()) {
            Collection<Localizable> messages = Collections2.transform(violations, new Function<ConstraintViolation<Object>, Localizable>() {
                @Override
                public Localizable apply(ConstraintViolation<Object> violation) {
                    return getViolationMessage(o, violation);
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
