package net.iteach.web.security;

import net.iteach.api.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	private final SecurityService securityService;
	
	@Autowired
	public LoginController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@RequestMapping("/login")
	public String login() {
		if (securityService.isAdminInitialized()) {
			return "login";
		} else {
			return "admin";
		}
	}
	
	@RequestMapping("/login_error")
	public String login_error (Model model) {
		model.addAttribute("error", Boolean.TRUE);
		return "login";
	}
	
}
