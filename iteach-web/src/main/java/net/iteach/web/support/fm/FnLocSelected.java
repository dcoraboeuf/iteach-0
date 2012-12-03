package net.iteach.web.support.fm;

import java.util.List;
import java.util.Locale;

import net.iteach.web.locale.CurrentLocale;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnLocSelected implements TemplateMethodModel {
	
	private final CurrentLocale currentLocale;
	
	@Autowired
	public FnLocSelected(CurrentLocale currentLocale) {
		this.currentLocale = currentLocale;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.isEmpty(), "List of arguments must be empty");
		// Gets the locale from the context
		Locale locale = currentLocale.getCurrentLocale();
		// Gets the value
		return locale.getLanguage().toLowerCase();
	}

}
