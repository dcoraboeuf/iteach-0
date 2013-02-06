package net.iteach.web.support;

import net.iteach.core.model.UserMessage;
import net.sf.jstring.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class ErrorController extends AbstractGUIController {

    private final Strings strings;

    @Autowired
    public ErrorController(ErrorHandler errorHandler, Strings strings) {
        super(errorHandler);
        this.strings = strings;
    }

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public ModelAndView contact (Locale locale, @RequestParam String uuid, @RequestParam String error, @RequestParam String message) {
        // Goes back to the home page with a message
        return new ModelAndView("index", "message", UserMessage.info(strings.get(locale, "error.contact.success")));
    }

}
