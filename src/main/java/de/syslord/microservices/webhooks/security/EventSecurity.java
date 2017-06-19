package de.syslord.microservices.webhooks.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("eventSecurity")
public class EventSecurity {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EventSecurity.class);

	public boolean hasEventPermission(String key) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean permitted = authentication.getAuthorities().stream()
			.map(auth -> auth.getAuthority())
			.filter(auth -> auth.equals("EVENT_" + key))
			.findFirst().isPresent();

		return permitted;
	}
}