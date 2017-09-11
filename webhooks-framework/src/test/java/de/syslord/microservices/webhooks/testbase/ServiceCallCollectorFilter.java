package de.syslord.microservices.webhooks.testbase;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallCollectorFilter implements Filter {

	@Autowired
	private ServiceCallCollector serviceCallCollector;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing.
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		String url = null;
		String queryString = null;
		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			url = httpServletRequest.getRequestURL().toString();
			queryString = httpServletRequest.getQueryString();
		}

		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				UserDetails.class.isAssignableFrom(authentication.getPrincipal().getClass())) {

			UserDetails principal = (UserDetails) authentication.getPrincipal();
			username = principal.getUsername();
		}

		serviceCallCollector.called(url, queryString, username);

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		// do nothing.
	}
}