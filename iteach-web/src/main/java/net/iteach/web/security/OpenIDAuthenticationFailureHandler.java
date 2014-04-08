package net.iteach.web.security;

import net.iteach.api.UserNonVerifiedOrDisabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("openIDAuthenticationFailureHandler")
public class OpenIDAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(OpenIDAuthenticationFailureHandler.class);

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
                    exception.getAuthentication().getName());
            // redirect to create account page
            redirectStrategy.sendRedirect(request, response, "/registration_openid");
        } else if (exception instanceof UserNonVerifiedOrDisabledException) {
            // Goes back to the login page with an error message
            redirectStrategy.sendRedirect(request, response, "/login_error");
        } else {
            // Logs the message
            logger.error("General OpenID failure", exception);
            // Goes back to the login page with an error message
            redirectStrategy.sendRedirect(request, response, "/login_openid_failed");
        }
    }
}
