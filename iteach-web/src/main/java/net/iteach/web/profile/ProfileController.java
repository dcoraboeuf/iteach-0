package net.iteach.web.profile;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/profile")
public class ProfileController {
	
	/**
	 * Profile page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String profile (Model model) {
		// OK
		return "profile";
	}

}
