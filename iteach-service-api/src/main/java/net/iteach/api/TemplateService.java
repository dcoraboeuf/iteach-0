package net.iteach.api;

import java.util.Locale;

import net.iteach.api.model.TemplateModel;

public interface TemplateService {

	String generate(String templateId, Locale locale, TemplateModel templateModel);

}
