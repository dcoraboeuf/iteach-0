package net.iteach.web.index;

import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends AbstractGUIController {
	
	@Autowired
	public IndexController(ErrorHandler errorHandler) {
		super(errorHandler);
	}

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

}
