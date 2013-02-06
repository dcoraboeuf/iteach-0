package net.iteach.web.support;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.iteach.api.model.ErrorMessage;
import net.sf.jstring.support.CoreException;
import org.springframework.web.servlet.ModelAndView;

public interface ErrorHandler {

    ModelAndView getErrorModelAndView(HttpServletRequest request, Locale locale, Exception ex);

	ErrorMessage handleError(HttpServletRequest request, Locale locale, Exception ex);

	String displayableError(CoreException ex, Locale locale);

	boolean canHandleAccessDenied();

}
