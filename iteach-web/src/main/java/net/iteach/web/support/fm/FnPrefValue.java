package net.iteach.web.support.fm;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import net.iteach.api.PreferenceService;
import net.iteach.core.model.PreferenceKey;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class FnPrefValue implements TemplateMethodModel {

    private final PreferenceService preferenceService;

    @Autowired
    public FnPrefValue(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
        // Checks
        Validate.notNull(list, "List of arguments is required");
        Validate.isTrue(list.size() == 1, "List of arguments must contain 1 element");
        // Gets the preference key
        PreferenceKey key = PreferenceKey.valueOf((String) list.get(0));
        // Gets the value
        return preferenceService.getPreference(key);
    }

}
