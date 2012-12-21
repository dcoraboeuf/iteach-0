package net.iteach.core.validation;

import net.sf.jstring.MultiLocalizable;
import net.sf.jstring.support.CoreException;

public class ValidationException extends CoreException {

    public ValidationException(MultiLocalizable multiLocalizable) {
        super(multiLocalizable);
    }

}
