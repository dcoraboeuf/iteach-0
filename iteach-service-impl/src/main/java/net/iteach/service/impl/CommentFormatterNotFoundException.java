package net.iteach.service.impl;

import net.iteach.core.model.CommentFormat;
import net.iteach.utils.InputException;

public class CommentFormatterNotFoundException extends InputException {

	public CommentFormatterNotFoundException(CommentFormat format) {
		super(format);
	}

}
