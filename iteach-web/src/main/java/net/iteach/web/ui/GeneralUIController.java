package net.iteach.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.iteach.api.CommentsService;
import net.iteach.core.model.CommentPreview;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.GeneralUI;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;

@Controller
@RequestMapping("/ui")
public class GeneralUIController extends AbstractUIController implements GeneralUI {
	
	private final CommentsService commentsService;

	@Autowired
	public GeneralUIController(SecurityUtils securityUtils, ErrorHandler errorHandler, Strings strings, CommentsService commentsService) {
		super(securityUtils, errorHandler, strings);
		this.commentsService = commentsService;
	}

	@Override
	@RequestMapping(value = "/comment/preview", method = RequestMethod.POST)
	public @ResponseBody CommentPreview commentPreview(@RequestBody CommentPreview request) {
		return commentsService.getPreview(request);
	}

}
