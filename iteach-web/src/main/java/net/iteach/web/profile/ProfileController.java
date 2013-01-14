package net.iteach.web.profile;

import java.util.Locale;

import net.iteach.api.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	/**
	 * Requests a password change
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String password (Locale locale) {
		// Request
		profileService.passwordRequest(locale);
		// OK
		return "passwordRequest";
	}

	/**
	 * The user to enter his old password and his new password
	 */
	@RequestMapping(value = "/passwordChange/{token}", method = RequestMethod.GET)
	public String passwordChangeForm (Model model, @PathVariable String token) {
		model.addAttribute("token", token);
		return "passwordChange";
	}

}
