package de.syslord.microservices.webhooks.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.syslord.microservices.webhooks.events.interfaces.AsyncServiceCaller;
import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.WebHookException;
import de.syslord.microservices.webhooks.storage.interfaces.WebHookStorage;
import de.syslord.microservices.webhooks.testbase.security.TestSecurityConfig;
import de.syslord.microservices.webhooks.utils.ServiceCall;
import de.syslord.microservices.webhooksinclude.defaultservicecaller.SimpleServiceCaller;

@EnableAutoConfiguration
@ComponentScan({ "de.syslord.microservices.webhooks" })
@Configuration
public class WebHookTestConfig {

	private static Map<String, String> usernameAuthorities = Maps.newHashMap();

	public static final String TESTUSER_A = "testuserA";

	public static final String TESTUSER_B = "testuserB";

	public static final String TESTUSER_C = "testuserC";

	public static final String CALLME_ENDPOINT_USER = "callme_user";

	static {
		usernameAuthorities.put(TESTUSER_A, "ROLE_USER,ROLE_SUBSCRIBER,EVENT_X");
		usernameAuthorities.put(TESTUSER_B, "ROLE_USER,ROLE_SUBSCRIBER,EVENT_Y,EVENT_Z");
		usernameAuthorities.put(TESTUSER_C, "ROLE_USER,ROLE_SUBSCRIBER,EVENT_X,EVENT_Z");

		usernameAuthorities.put(CALLME_ENDPOINT_USER, "ROLE_USER");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public SimpleServiceCaller asyncServiceCaller(RestTemplate restTemplate) {
		return new SimpleServiceCaller(restTemplate);
	}

	@Bean
	// method may not be named like the result type! (weird spring problem)
	public AsyncServiceCaller concreteAsyncServiceCaller(SimpleServiceCaller simpleServiceCaller) {
		// make synchronous calls for test
		return new AsyncServiceCaller() {

			@Override
			public void enqueue(ServiceCall serviceCall) {
				simpleServiceCaller.call(serviceCall);
			}
		};
	}

	@Bean
	public TestSecurityConfig getTestSecurityConfig() {
		// gets valid user and the respective authorities, also the unsecured endpoints
		return new TestSecurityConfig() {

			@Override
			protected String getUserAuthorities(String username) {
				String authorities = usernameAuthorities.get(username);
				return authorities;
			}

			@Override
			protected String[] getUnsecuredRequestMappings() {
				return new String[] {};
			}
		};
	}

	@Bean
	public WebHookStorage getWebHookStorage() {
		// map as webhook storage
		return new WebHookStorage() {

			List<WebHookDto> hooks = Lists.newArrayList();

			@Override
			public void add(String username, WebHookDto webHookDto) throws WebHookException {
				WebHookDto withOwner = webHookDto.withOwner(username);
				hooks.add(withOwner);
			}

			@Override
			public void patch(String username, WebHookDto webHookDto) throws WebHookException {
				//
			}

			@Override
			public void delete(String username, String id) {
				//
			}

			@Override
			public List<WebHookDto> getWebHooksForUser(String username) {
				return hooks.stream()
					.filter(h -> h.getOwner().equals(username))
					.collect(Collectors.toList());
			}

			@Override
			public List<WebHookDto> getWebHooksForEvent(String eventname) {
				return hooks.stream()
					.filter(h -> h.getEvent().equals(eventname))
					.collect(Collectors.toList());
			}

		};
	}

}
