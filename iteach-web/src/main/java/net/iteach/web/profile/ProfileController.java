package net.iteach.web.profile;

import net.iteach.api.ProfileService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.PreferenceKey;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    public String profile(Model model) {
        // Profile
        model.addAttribute("profile", profileService.getProfile());
        // OK
        return "profile";
    }

    /**
     * Update of preferences
     */
    @RequestMapping(value = "/preferences", method = RequestMethod.POST)
    public String preferences(Locale locale, Model model, RedirectAttributes redirectAttributes, boolean weekend) {
        // Gets the preferences keys
        Map<PreferenceKey, String> preferences = new HashMap<>();
        // Week-end
        preferences.put(PreferenceKey.PLANNING_WEEKEND, String.valueOf(weekend));
        // Saves the preferences
        profileService.savePreferences(preferences);
        // Reloads the profile with a success message
        redirectAttributes.addFlashAttribute("message", UserMessage.success(strings.get(locale, "profile.preferences.saved")));
        // Redirects
        return "redirect:/gui/profile";
    }

    /**
     * Requests a password change
     */
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String password(Locale locale) {
        // Request
        profileService.passwordRequest(locale);
        // OK
        return "passwordRequest";
    }

    /**
     * The user requests a form to enter his old password and his new password
     */
    @RequestMapping(value = "/passwordChange/{token}", method = RequestMethod.GET)
    public String passwordChangeForm(Model model, @PathVariable String token) {
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
    public String passwordChangeForm(Locale locale, Model model, @RequestParam String token, @RequestParam String oldPassword, @RequestParam String newPassword) {
        // Change
        Ack ack = profileService.passwordChange(token, oldPassword, newPassword);
        if (ack.isSuccess()) {
            // OK
            model.addAttribute("message", UserMessage.success(strings.get(locale, "page.passwordRequest.ok")));
            // Goes back to profile
            return profile(model);
        } else {
            model.addAttribute("message", UserMessage.error(strings.get(locale, "page.passwordRequest.error")));
            model.addAttribute("token", token);
            return "passwordChange";
        }
    }

}
