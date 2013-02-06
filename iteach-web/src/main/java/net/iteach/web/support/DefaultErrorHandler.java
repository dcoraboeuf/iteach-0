package net.iteach.web.support;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.iteach.api.CommunicationService;
import net.iteach.api.model.ErrorMessage;
import net.iteach.core.security.SecurityUtils;
import net.sf.jstring.Strings;
import net.sf.jstring.support.CoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class DefaultErrorHandler implements ErrorHandler {

	private final Logger logger = LoggerFactory.getLogger("User");
	
	private final Strings strings;
	private final SecurityUtils securityUtils;
    private final CommunicationService communicationService;
	
	@Autowired
	public DefaultErrorHandler(Strings strings, SecurityUtils securityUtils, CommunicationService communicationService) {
		this.strings = strings;
		this.securityUtils = securityUtils;
        this.communicationService = communicationService;
    }
	
	@Override
	public boolean canHandleAccessDenied() {
		return securityUtils.isLogged();
	}

    @Override
    public ModelAndView getErrorModelAndView(HttpServletRequest request, Locale locale, Exception ex) {
        // Error message
        ErrorMessage error = handleError (request, locale, ex);
        // Model
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", error);
        // Prepare communication for the error
        String token = communicationService.sendError(error);
        mav.addObject("token", token);
        // Prepare the view
        WebUtils.prepareModelAndView(mav, request);
        // OK
        return mav;
    }

	@Override
	public ErrorMessage handleError(HttpServletRequest request, Locale locale, Exception ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Error message
		String displayMessage;
		String loggedMessage;
		boolean stackTrace;
		if (ex instanceof CoreException) {
			loggedMessage = ((CoreException)ex).getLocalizedMessage(strings, Locale.ENGLISH);
			stackTrace = false;
			displayMessage = ((CoreException)ex).getLocalizedMessage(strings, locale);
		} else {
			loggedMessage = ex.getMessage();
			stackTrace = true;
			// Gets a display message for this exception class
			String messageKey = ex.getClass().getName();
			if (strings.isDefined(locale, messageKey)) {
				displayMessage = strings.get(locale, messageKey, false);
			} else {
				displayMessage = strings.get(locale, "general.error.technical");
			}
		}
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		String formattedLoggedMessage = String.format("[%s] %s", uuid, loggedMessage);
		if (stackTrace) {
			logger.error(formattedLoggedMessage, ex);
		} else {
			logger.error(formattedLoggedMessage);
		}
		// OK
		return new ErrorMessage(stackTrace, uuid, displayMessage);
	}
	
	@Override
	public String displayableError(CoreException ex, Locale locale) {
		return ex.getLocalizedMessage(strings, locale);
	}

}
