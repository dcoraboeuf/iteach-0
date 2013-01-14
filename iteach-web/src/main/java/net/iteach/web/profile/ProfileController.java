package net.iteach.web.profile;

import java.util.Locale;

import net.iteach.api.ProfileService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/gui/profile")
public class ProfileController extends AbstractGUIController {
	
	private final ProfileService profileService;
	private final Strings strings;
		
	@Autowired
	public ProfileController(ErrorHandler errorHandler, ProfileService profileService, Strings strings) {
		super(errorHandler);
		this.profileService = profileService;
		this.strings = strings;
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
	 * The user requests a form to enter his old password and his new password
	 */
	@RequestMapping(value = "/passwordChange/{token}", method = RequestMethod.GET)
	public String passwordChangeForm (Model model, @PathVariable String token) {
		// Checks the token
		profileService.passwordChangeCheck(token);
		// Displays the form
		model.addAttribute("token", token);
		return "passwordChange";
	}

	/**
	 * The user has entered his old password and his new password
	 */
	@RequestMapping(value = "/passwordChange", method = RequestMethod.POST)
	public String passwordChangeForm (Locale locale, Model model, @RequestParam String token, @RequestParam String oldPassword, @RequestParam String newPassword) {
		// Change
		profileService.passwordChange(token, oldPassword, newPassword);
		// OK
		model.addAttribute("message", UserMessage.success(strings.get(locale, "page.passwordRequest.ok")));
		// Goes back to profile
		return profile(model);
	}

}
