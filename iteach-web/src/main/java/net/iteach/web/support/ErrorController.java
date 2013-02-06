package net.iteach.web.support;

import net.iteach.api.CommunicationService;
import net.iteach.core.model.Ack;
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

    private final CommunicationService communicationService;
    private final Strings strings;

    @Autowired
    public ErrorController(ErrorHandler errorHandler, CommunicationService communicationService, Strings strings) {
        super(errorHandler);
        this.communicationService = communicationService;
        this.strings = strings;
    }

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public ModelAndView contact (Locale locale, @RequestParam String token, @RequestParam String uuid, @RequestParam String error, @RequestParam String message) {
        // Sends the message
        Ack ack = communicationService.sendErrorMessage(token, uuid, error, message);
        // Goes back to the home page with a message
        UserMessage userMessage;
        if (ack.isSuccess()) {
            userMessage = UserMessage.info(strings.get(locale, "error.contact.success"));
        } else {
            userMessage = UserMessage.error(strings.get(locale, "error.contact.failed"));
        }
        return new ModelAndView("index", "message", userMessage);
    }

}
