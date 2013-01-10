package net.iteach.web.security;

import java.util.Locale;

import net.iteach.core.model.UserMessage;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	private final Strings strings;
	
	@Autowired
	public LoginController(Strings strings) {
		this.strings = strings;
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/login_error")
	public String login_error (Locale locale, Model model) {
		model.addAttribute("message", UserMessage.error(strings.get(locale, "login.error")));
		return "login";
	}
	
}
