package net.iteach.web.form;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Serves the content of dialogs.
 */
@Controller
public class FormController {
	
	private final AtomicInteger id = new AtomicInteger();

	// TODO HTTP cache management

	@RequestMapping(value = "/gui/form/{name}", method = RequestMethod.GET)
	public String form(Model model, @PathVariable String name) {
		model.addAttribute("formId", id.incrementAndGet());
		return String.format("form/%s", name);
	}

}
