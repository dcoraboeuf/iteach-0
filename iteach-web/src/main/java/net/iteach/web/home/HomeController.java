package net.iteach.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/home")
public class HomeController {
	
	/**
	 * Home page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(Model model) {
		// OK
		return "home";
	}

}
