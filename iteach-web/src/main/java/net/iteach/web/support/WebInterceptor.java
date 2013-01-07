package net.iteach.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("webInterceptor")
public class WebInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView) throws Exception {
		// Adds the base URL to the model
		if (modelAndView != null) {
			modelAndView.addObject("baseURL", WebUtils.getBaseURL(request));
		}
		// OK
		super.postHandle(request, response, handler, modelAndView);
	}

}
