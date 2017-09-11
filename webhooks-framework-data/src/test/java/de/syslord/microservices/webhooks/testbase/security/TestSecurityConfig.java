package de.syslord.microservices.webhooks.testbase.security;

import org.springframework.security.core.userdetails.UserDetails;

public abstract class TestSecurityConfig {

	public UserDetails authenticateUser(String username) {
		String authorities = getUserAuthorities(username);

		if (authorities == null || authorities.isEmpty()) {
			return null;
		} else {
			return WebHooksTestUserFactory.createUser(username, authorities);
		}
	}

	protected abstract String getUserAuthorities(String username);

	protected abstract String[] getUnsecuredRequestMappings();

}
