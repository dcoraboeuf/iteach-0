package net.iteach.api.model;

import lombok.Data;

@Data
public class ErrorMessage {

    private final boolean report;
	private final String uuid;
	private final String message;

}
