package net.iteach.core.security;

public interface SecurityUtils {

	boolean isLogged();

	User getCurrentUser();

	int getCurrentUserId();

}
