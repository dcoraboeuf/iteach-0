package net.iteach.web.form;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Serves the content of dialogs.
 */
@Controller
public class FormController {

	// TODO HTTP cache management

	@RequestMapping(value = "/gui/form/{name}", method = RequestMethod.GET)
	public String form(@PathVariable String name) {
		return String.format("form/%s", name);
	}

}
