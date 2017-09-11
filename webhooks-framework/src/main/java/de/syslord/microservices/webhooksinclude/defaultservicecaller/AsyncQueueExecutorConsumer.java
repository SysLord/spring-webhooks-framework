package de.syslord.microservices.webhooksinclude.defaultservicecaller;

import de.syslord.microservices.webhooks.utils.ServiceCall;

public interface AsyncQueueExecutorConsumer {

	void accept(ServiceCall serviceCall);

}