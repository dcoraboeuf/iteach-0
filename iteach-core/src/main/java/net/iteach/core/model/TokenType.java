package net.iteach.core.model;

public enum TokenType {
	/**
	 * The user has just been created and he must enter his new password.
	 */
	REGISTRATION,
	
	/**
	 * The user has requested a new password
	 */
	PASSWORD_REQUEST,

    /**
     * An error has been generated and an associated token can be used to send an error report to the admin by the user.
     */
    ERROR;
}