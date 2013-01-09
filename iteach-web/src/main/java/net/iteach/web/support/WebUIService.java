package net.iteach.web.support;

import static java.lang.String.format;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.iteach.api.UIService;
import net.iteach.core.model.TokenType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class WebUIService implements UIService {
	
	private final Logger logger = LoggerFactory.getLogger(UIService.class);

	@Override
	public String getLink(TokenType type, String token) {
		// Gets the request from the content
		HttpServletRequest request = WebInterceptor.getCurrentRequest();
		// Logs everything about the request
		// log(request);
		// Gets the root URL
		String root = WebUtils.getBaseURL(request);
		logger.debug("[ui] Root URL: {}", root);
		// Building the variable part
		String query;
		switch (type) {
		case REGISTRATION:
			query = format("account/registration/%s", token);
			break;
		default:
			throw new IllegalStateException("TokenType is not supported: " + type);
		}
		// OK
		return format("%s/%s", root, query);
	}

	@SuppressWarnings("unused")
	private void log(HttpServletRequest request) {
		logger.debug("[request] Context path: {}", request.getContextPath());
		logger.debug("[request] Server name: {}", request.getServerName());
		logger.debug("[request] Server port: {}", request.getServerPort());
		logger.debug("[request] Request URL: {}", request.getRequestURL());
		logger.debug("[request] Protocol: {}", request.getProtocol());
		logger.debug("[request] Scheme: {}", request.getScheme());
		logger.debug("[request] Servlet path: {}", request.getServletPath());
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			@SuppressWarnings("unchecked")
			Enumeration<String> values = request.getHeaders(name);
			while (values.hasMoreElements()) {
				String value = values.nextElement();
				logger.debug("[request] Header {} = {}", name, value);
			}
		}
	}

}