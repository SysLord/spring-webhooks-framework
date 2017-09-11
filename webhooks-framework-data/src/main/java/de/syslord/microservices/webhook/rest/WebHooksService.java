package de.syslord.microservices.webhook.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.syslord.microservices.webhooks.events.EventExample;
import de.syslord.microservices.webhooks.events.Events;
import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.WebHookException;
import de.syslord.microservices.webhooks.storage.WebHookRegistrationsDto;
import de.syslord.microservices.webhooks.storage.interfaces.WebHookStorage;

@RestController
public class WebHooksService {

	@Autowired
	private WebHookStorage webHookStorage;

	@Autowired
	private Events events;

	// TODO spring properties!

	@GetMapping(
			path = "/webhooks",
			name = "/webhooks",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<WebHookRegistrationsDto> getSubscriptions(Principal principal) {

		String username = principal.getName();
		return getUserWebHooks(username);
	}

	@PutMapping(
			path = "/webhooks",
			name = "/webhooks",
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<WebHookRegistrationsDto> createWebHook(
			@RequestBody WebHookDto webHook,
			Principal principal) throws WebHookException {

		String username = principal.getName();
		webHookStorage.add(username, webHook);

		return getUserWebHooks(username);
	}

	@PatchMapping(
			path = "/webhooks",
			name = "/webhooks",
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<WebHookRegistrationsDto> patchSubscription(
			@RequestBody WebHookDto webHookDto,
			Principal principal) throws WebHookException {

		String username = principal.getName();
		webHookStorage.patch(username, webHookDto);

		return getUserWebHooks(username);
	}

	@DeleteMapping(
			path = "/webhooks/{name}",
			name = "/webhooks/{name}",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<WebHookRegistrationsDto> deletewebHook(
			@PathVariable String name,
			Principal principal) {

		String username = principal.getName();
		webHookStorage.delete(username, name);

		return getUserWebHooks(username);
	}

	private ResponseEntity<WebHookRegistrationsDto> getUserWebHooks(String username) {
		List<WebHookDto> webHooksForUser = webHookStorage.getWebHooksForUser(username);
		WebHookRegistrationsDto webHooksDto = new WebHookRegistrationsDto(webHooksForUser);
		return ResponseEntity.ok().body(webHooksDto);
	}

	@GetMapping(
			path = "/webhooks/events",
			name = "/webhooks/events",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getPossibleEvents() {
		List<EventExample> examples = events.getExamples();
		return ResponseEntity.ok().body(examples);
	}

}