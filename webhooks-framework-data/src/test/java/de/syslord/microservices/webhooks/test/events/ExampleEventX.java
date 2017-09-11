package de.syslord.microservices.webhooks.test.events;

import de.syslord.microservices.webhooks.events.WebHookEvent;

public class ExampleEventX extends WebHookEvent {

	public static final String NAME = "X";

	private ExampleEventX() {
		//
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static ExampleEventX create(String parameter) {
		ExampleEventX exampleEvent = new ExampleEventX();
		exampleEvent.addPlaceholder("{parameter}", parameter);
		return exampleEvent;
	}

}
