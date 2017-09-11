package de.syslord.microservices.webhooks.events.interfaces;

import de.syslord.microservices.webhooks.utils.ServiceCall;

public interface AsyncServiceCaller {

	void enqueue(ServiceCall serviceCall);

}
