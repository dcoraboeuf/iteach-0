package net.iteach.service.template;

import net.sf.jstring.support.CoreException;

public class TemplateMergeException extends CoreException {

	public TemplateMergeException(String templateId, Exception ex) {
		super(ex, templateId, ex);
	}

}
