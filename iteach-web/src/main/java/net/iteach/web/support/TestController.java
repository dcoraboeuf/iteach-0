package net.iteach.web.support;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.iteach.api.MessagePost;
import net.iteach.api.model.MessageChannel;
import net.iteach.core.RunProfile;
import net.iteach.core.model.Message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controllers is used by the integration tests
 * 
 */
@Controller
@RequestMapping("/test")
@Profile({RunProfile.TEST, RunProfile.IT})
public class TestController implements MessagePost {

	private final Map<String, Message> messages = new LinkedHashMap<String, Message>();

	/**
	 * Supports all channels
	 */
	@Override
	public boolean supports(MessageChannel channel) {
		return true;
	}

	@Override
	public synchronized void post(Message message, String destination) {
		messages.put(destination, message);
	}

	/**
	 * Collects the message for a user
	 */
	@RequestMapping("/message/{email:.*}")
	public synchronized @ResponseBody Message message(HttpServletResponse response, @PathVariable String email) throws IOException {
		Message latestMessage = messages.get(email);
		if (latestMessage != null) {
			return latestMessage;
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

}
