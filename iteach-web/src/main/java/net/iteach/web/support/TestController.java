package net.iteach.web.support;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.iteach.api.support.InMemoryPost;
import net.iteach.core.RunProfile;
import net.iteach.core.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
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
public class TestController {
	
	private final InMemoryPost inMemoryPost;
		
	@Autowired
	public TestController(InMemoryPost inMemoryPost) {
		this.inMemoryPost = inMemoryPost;
	}

	/**
	 * Collects the message for a user
	 */
	@RequestMapping("/message/{email:.*}")
	public synchronized @ResponseBody Message message(HttpServletResponse response, @PathVariable String email) throws IOException {
		Message latestMessage = inMemoryPost.getMessage(email);
		if (latestMessage != null) {
			return latestMessage;
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

}
