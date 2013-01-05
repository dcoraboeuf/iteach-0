package net.iteach.core.model;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class CommentPreview {
	
	private final CommentFormat format;
	
	@Wither
	private final String content;

}
