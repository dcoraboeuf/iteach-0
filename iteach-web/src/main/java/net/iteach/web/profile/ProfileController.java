package net.iteach.web.profile;

import net.iteach.api.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/profile")
public class ProfileController {
	
	private final ProfileService profileService;	
	
	@Autowired
	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	/**
	 * Profile page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String profile (Model model) {
		// Profile
		model.addAttribute("profile", profileService.getProfile());
		// OK
		return "profile";
	}

}
