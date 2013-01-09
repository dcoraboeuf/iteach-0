package net.iteach.web.security;

import net.iteach.api.SecurityService;
import net.iteach.core.model.RegistrationCompletionForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	private final SecurityService securityService;
	
	
	@Autowired
	public AccountController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * The user has received his registration form and wants to complete it. He
	 * will receive his data and will have to enter his password again.
	 */
	@RequestMapping(value = "/registration/{token}", method = RequestMethod.GET)
	public String completeRegistration (Model model, @PathVariable String token) {
		// Checks and gets the registration completion form
		RegistrationCompletionForm form = securityService.getRegistrationCompletionForm(token);
		model.addAttribute("form", form);
		// OK
		return "completeRegistrationForm";
	}

}
