package net.iteach.web.security;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.iteach.api.SecurityService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.utils.InputException;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.support.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

	private final SecurityService securityService;
	private final ErrorHandler errorHandler; 

	@Autowired
	public RegistrationController(SecurityService securityService, ErrorHandler errorHandler) {
		this.securityService = securityService;
		this.errorHandler = errorHandler;
	}
	
	@ExceptionHandler(InputException.class)
	public ModelAndView onInputException (HttpServletRequest request, HttpSession session, Locale locale, Exception ex) {
		// Model
		ModelAndView mav = new ModelAndView("registrationForm");
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Error
		mav.addObject("error", error);
		// Mode
		mav.addObject("mode", (AuthenticationMode) session.getAttribute(SessionKeys.USER_SECURITY_MODE));
		// Identifier
		String identifier = (String) session.getAttribute(SessionKeys.USER_OPENID_CREDENTIAL);
		if (identifier == null) {
			identifier = "";
		}
		mav.addObject("identifier", identifier);
		// OK
		return mav;
	}

	/**
	 * @see OpenIDAuthenticationFailureHandler
	 */
	@RequestMapping(value = "/registration_openid", method = RequestMethod.GET)
	public String registrationOpenID(Model model, HttpSession session) {
		String identifier = (String) session.getAttribute(SessionKeys.USER_OPENID_CREDENTIAL);
		model.addAttribute("identifier", identifier);
		model.addAttribute("mode", AuthenticationMode.openid);
		return "registrationForm";
	}

	/**
	 * Used to register as a simple user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("mode", AuthenticationMode.password);
		return "registrationForm";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(Model model, HttpSession session, AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
		// Session
		session.setAttribute(SessionKeys.USER_SECURITY_MODE, mode);
		// Registration
		securityService.register (mode, identifier, firstName, lastName, email, password);
		// OK
		return loginOkNow(model);
	}

	@RequestMapping(value = "/init", method = RequestMethod.POST)
	public String init(Model model, String firstName, String lastName, String email, String password) {
		// Registration
		securityService.init (firstName, lastName, email, password);
		// OK
		return loginOkNow(model);
	}

	/**
	 * Redirect to the login with a message saying that the registration has been successful
	 */
	protected String loginOkNow(Model model) {
		model.addAttribute("registrationOK", Boolean.TRUE);
		return "login";
	}

}
