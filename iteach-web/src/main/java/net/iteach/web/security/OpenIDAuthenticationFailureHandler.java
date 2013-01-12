package net.iteach.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("openIDAuthenticationFailureHandler")
public class OpenIDAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	@SuppressWarnings("deprecation")
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		if (exception instanceof UsernameNotFoundException
				&& exception.getAuthentication() instanceof OpenIDAuthenticationToken
				&& ((OpenIDAuthenticationToken) exception.getAuthentication()).getStatus().equals(OpenIDAuthenticationStatus.SUCCESS)) {
			request.getSession(true).setAttribute(
					"USER_OPENID_CREDENTIAL",
					((UsernameNotFoundException) exception).getAuthentication().getName());
			// redirect to create account page
			redirectStrategy.sendRedirect(request, response, "/registration_openid");
		} else {
			// Goes back to the login page with an error message
			redirectStrategy.sendRedirect(request, response, "/login_openid_failed");
		}
	}
}
