package net.iteach.core.model;

import lombok.Data;

@Data
public class CommentPreview {

    private final CommentFormat format;
    private final String content;

    public CommentPreview withContent(String content) {
        return new CommentPreview(format, content);
    }
}
