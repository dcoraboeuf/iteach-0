package net.iteach.api.model.copy;

import lombok.Data;

import java.util.List;

@Data
public abstract class WithComments {

    private final List<Comment> comments;

}