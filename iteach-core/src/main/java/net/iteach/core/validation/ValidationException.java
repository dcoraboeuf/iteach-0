package net.iteach.core.validation;

import net.iteach.utils.InputException;
import net.sf.jstring.MultiLocalizable;

public class ValidationException extends InputException {

    public ValidationException(MultiLocalizable multiLocalizable) {
        super(multiLocalizable);
    }

}
