package net.iteach.api;

import net.iteach.api.model.TemplateModel;

public interface TemplateService {

	String generate(String templateId, TemplateModel templateModel);

}
