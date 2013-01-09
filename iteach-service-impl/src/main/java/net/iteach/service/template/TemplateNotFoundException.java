package net.iteach.service.template;

import java.io.IOException;

import net.sf.jstring.support.CoreException;

public class TemplateNotFoundException extends CoreException {

	public TemplateNotFoundException(String templateId, IOException ex) {
		super(ex, templateId, ex);
	}

}
