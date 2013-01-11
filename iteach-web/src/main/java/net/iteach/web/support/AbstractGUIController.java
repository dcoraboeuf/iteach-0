package net.iteach.web.support;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// TODO Spring 3.2 - the hierarchy can be deprecated in favor of the annotations

public abstract class AbstractGUIController extends AbstractController {

	public AbstractGUIController(ErrorHandler errorHandler) {
		super(errorHandler);
	}

	/**
	 * Generic error handler (<i>catch-all/i>)
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView onException (HttpServletRequest request, Locale locale, Exception ex) {
		// Forwards access denied exceptions
		if (ex instanceof AccessDeniedException) {
			throw (AccessDeniedException) ex;
		}
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Model
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("error", error);
		// OK
		return mav;
	}
	

}
