package net.iteach.web.security;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {
	
	@RequestMapping(value = "/registration_openid")
	public String registrationOpenID (Model model, HttpSession session) {
		String identifier = (String) session.getAttribute("USER_OPENID_CREDENTIAL");
		model.addAttribute("identifier", identifier);
		model.addAttribute("mode", "openid");
		return "registrationForm";
	}

}
