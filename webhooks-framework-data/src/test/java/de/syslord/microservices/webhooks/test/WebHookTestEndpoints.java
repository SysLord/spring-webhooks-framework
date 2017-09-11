package de.syslord.microservices.webhooks.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.syslord.microservices.webhooks.events.EventExample;
import de.syslord.microservices.webhooks.events.Events;

@RestController
public class WebHookTestEndpoints {

	public static final String CALL_ME_BY_HOOK = "/callme";

	public static final String GET_EVENTS = "/events";

	@Autowired
	private Events events;

	@PreAuthorize("authentication.name == '" + WebHookTestConfig.CALLME_ENDPOINT_USER + "'")
	@GetMapping(
			path = CALL_ME_BY_HOOK,
			name = CALL_ME_BY_HOOK)
	public ResponseEntity<String> getSubscriptions() {
		return ResponseEntity.ok().body("OK");
	}

	@GetMapping(
			path = GET_EVENTS,
			name = GET_EVENTS,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<EventExample>> getEvents() {
		return ResponseEntity.ok().body(events.getExamples());
	}
}