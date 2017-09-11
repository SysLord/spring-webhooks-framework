package de.syslord.microservices.webhooks.events;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import de.syslord.microservices.webhooks.events.interfaces.AsyncServiceCaller;
import de.syslord.microservices.webhooks.storage.WebHookCallMethod;
import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.interfaces.WebHookStorage;
import de.syslord.microservices.webhooks.utils.ServiceCall;
import de.syslord.microservices.webhooks.utils.StringUtil;

@Component
public class Events {

	private static final Logger logger = LoggerFactory.getLogger(Events.class);

	@Autowired
	private WebHookStorage webHookStorage;

	@Autowired
	private AsyncServiceCaller asyncServiceCaller;

	private final Map<String, Supplier<WebHookEvent>> eventsAndExamples = Maps.newHashMap();

	public void addEventType(String name, Supplier<WebHookEvent> exampleSupplier) {
		eventsAndExamples.put(name, exampleSupplier);
	}

	@PostFilter("@eventSecurity.hasEventPermission(filterObject.eventname)")
	public List<EventExample> getExamples() {

		List<EventExample> examples = eventsAndExamples.entrySet().stream()
			.map(entry -> new EventExample(entry.getKey(), entry.getValue().get()))
			.collect(Collectors.toList());

		return examples;
	}

	public void fireEvent(WebHookEvent webHookEvent) {
		List<WebHookDto> callList = webHookStorage.getWebHooksForEvent(webHookEvent.getName());

		String owners = callList.stream()
			.map(s -> s.getOwner())
			.distinct()
			.collect(Collectors.joining(", "));

		logger.info("fire event {}: {} calls for {}", webHookEvent.getName(), callList.size(), owners);

		callList.stream()
			.map(webHook -> {
				String address = webHookEvent.applyPlaceholders(webHook.getRemoteAddress());

				String postBody = webHook.getPostBody();
				boolean hasPostBody = !StringUtil.isEmptyTrimmed(postBody);

				Object body = null;
				if (hasPostBody) {
					body = webHookEvent.applyPlaceholders(webHook.getPostBody());
				} else if (webHookEvent.hasBody()) {
					body = webHookEvent.getBody();
				}

				WebHookCallMethod httpMethod = hasPostBody
						? WebHookCallMethod.POST
						: WebHookCallMethod.GET;

				ServiceCall serviceCall = new ServiceCall(
						httpMethod, address,
						webHook.getUsername(), webHook.getPassword(),
						body);
				return serviceCall;
			})
			.forEach(serviceCall -> asyncServiceCaller.enqueue(serviceCall));
	}

	public void clear() {
		eventsAndExamples.clear();
	}

}
