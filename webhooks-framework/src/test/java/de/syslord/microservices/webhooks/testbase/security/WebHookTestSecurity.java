package de.syslord.microservices.webhooks.testbase.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebHookTestSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private TestSecurityConfig testSecurityConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(testSecurityConfig.getUnsecuredRequestMappings()).permitAll()
			.anyRequest().fullyAuthenticated()
			.and()
			.httpBasic().and().csrf().disable();
	}

}