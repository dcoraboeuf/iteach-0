package net.iteach.web.support.fm;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import net.iteach.core.model.CoordinateType;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class FnCoordList implements TemplateMethodModel {

    @Override
    public Object exec(List list) throws TemplateModelException {
        // Checks
        Validate.notNull(list, "List of arguments is required");
        Validate.isTrue(list.isEmpty(), "List of arguments must be empty");
        // List of coordinate types
        return CoordinateType.values();
    }
}
