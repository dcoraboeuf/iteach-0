package net.iteach.web.support;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.iteach.core.security.SecurityUtils;
import net.iteach.utils.InputException;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class AbstractUIController extends AbstractController {

	protected final SecurityUtils securityUtils;
	private final Strings strings;

	@Autowired
	public AbstractUIController(SecurityUtils securityUtils,
                                ErrorHandler errorHandler,
                                Strings strings) {
		super(errorHandler);
		this.securityUtils = securityUtils;
		this.strings = strings;
	}

	protected ResponseEntity<String> getMessageResponse(String message) {
		// Header
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
		// OK
		return new ResponseEntity<String>(message, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InputException.class)
	public ResponseEntity<String> onInputException (HttpServletRequest request, Locale locale, InputException ex) {
		// Returns a message to display to the user
		String message = ex.getLocalizedMessage(strings, locale);
		// OK
		return getMessageResponse(message);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> onException (HttpServletRequest request, Locale locale, Exception ex) {
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Returns a message to display to the user
		String message = strings.get(locale, "general.error.full", error.getMessage(), error.getUuid());
		// Ok
		return getMessageResponse(message);
	}

}
