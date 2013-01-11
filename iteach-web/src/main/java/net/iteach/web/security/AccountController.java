package net.iteach.web.security;

import java.util.Locale;

import net.iteach.api.SecurityService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.UserMessage;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController extends AbstractGUIController {
	
	private final SecurityService securityService;
	private final Strings strings;
	
	
	@Autowired
	public AccountController(ErrorHandler errorHandler, SecurityService securityService, Strings strings) {
		super(errorHandler);
		this.securityService = securityService;
		this.strings = strings;
	}

	/**
	 * The user has received his registration form and wants to complete it.
	 */
	@RequestMapping(value = "/registration/{token}", method = RequestMethod.GET)
	public String completeRegistration (Locale locale, Model model, @PathVariable String token) {
		Ack ack = securityService.completeRegistration(token);
		UserMessage message;
		if (ack.isSuccess()) {
			message = UserMessage.success(strings.get(locale, "login.registrationConfirmed"));
		} else {
			message = UserMessage.error(strings.get(locale, "login.registrationConfirmationFailed"));
		}
		model.addAttribute("message", message);
		return "login";
	}

}
