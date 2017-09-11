package de.syslord.microservices.webhooks.test.events;

import de.syslord.microservices.webhooks.events.WebHookEvent;

public class ExampleEventY extends WebHookEvent {

	public static final String NAME = "Y";

	private ExampleEventY() {
		//
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static ExampleEventY create(String parameter) {
		ExampleEventY exampleEvent = new ExampleEventY();
		exampleEvent.addPlaceholder("{parameter}", parameter);
		return exampleEvent;
	}

}
