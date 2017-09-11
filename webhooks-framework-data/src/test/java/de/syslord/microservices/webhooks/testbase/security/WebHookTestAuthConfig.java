package de.syslord.microservices.webhooks.testbase.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class WebHookTestAuthConfig extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private TestSecurityConfig testSecurityConfig;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> getTestUser(username));
	}

	private UserDetails getTestUser(String username) {
		return testSecurityConfig.authenticateUser(username);
	}

}
