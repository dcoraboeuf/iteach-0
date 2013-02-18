package net.iteach.web.profile;

import net.iteach.api.PreferenceService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.PreferenceKey;
import net.iteach.core.security.SecurityUtils;
import net.iteach.web.support.AbstractUIController;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui/profile")
public class ProfileUIController extends AbstractUIController {

    private final PreferenceService preferenceService;

    @Autowired
    public ProfileUIController(SecurityUtils securityUtils, ErrorHandler errorHandler, Strings strings, PreferenceService preferenceService) {
        super(securityUtils, errorHandler, strings);
        this.preferenceService = preferenceService;
    }

    /**
     * Update of preference
     */
    @RequestMapping(value = "/preference/{key}/{value:.*}", method = RequestMethod.POST)
    public
    @ResponseBody
    Ack preference(@PathVariable PreferenceKey key, @PathVariable String value) {
        // Saves the preference
        preferenceService.setPreference(key, value);
        // Redirects
        return Ack.OK;
    }

}
