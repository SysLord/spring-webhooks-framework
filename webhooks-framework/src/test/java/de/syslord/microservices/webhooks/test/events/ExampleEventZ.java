package de.syslord.microservices.webhooks.test.events;

import de.syslord.microservices.webhooks.events.WebHookEvent;

public class ExampleEventZ extends WebHookEvent {

	public static final String NAME = "Z";

	private ExampleEventZ() {
		//
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static ExampleEventZ create(String parameter) {
		ExampleEventZ exampleEvent = new ExampleEventZ();
		exampleEvent.addPlaceholder("{parameter}", parameter);
		return exampleEvent;
	}

}
