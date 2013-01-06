package net.iteach.web.security;

import javax.servlet.http.HttpSession;

import net.iteach.api.SecurityService;
import net.iteach.api.model.AuthenticationMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {

	private final SecurityService securityService;

	@Autowired
	public RegistrationController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * @see OpenIDAuthenticationFailureHandler
	 */
	@RequestMapping(value = "/registration_openid", method = RequestMethod.GET)
	public String registrationOpenID(Model model, HttpSession session) {
		String identifier = (String) session.getAttribute("USER_OPENID_CREDENTIAL");
		model.addAttribute("identifier", identifier);
		model.addAttribute("mode", "openid");
		return "registrationForm";
	}

	/**
	 * Used to register as a simple user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("mode", "basic");
		return "registrationForm";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
		// Registration
		securityService.register (mode, identifier, firstName, lastName, email, password);
		// FIXME Redirect to the login with a message saying that the registration has been successful
		return "redirect:/login";
	}

}
