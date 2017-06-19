package de.syslord.microservices.webhooksinclude.defaultservicecaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;

import de.syslord.microservices.webhooks.utils.ServiceCall;

@ConditionalOnBean({ HystrixCommandAspect.class })
@Component
public class HystrixAsyncConsumer implements AsyncQueueExecutorConsumer {

	private static final Logger logger = LoggerFactory.getLogger(HystrixAsyncConsumer.class);

	private static final String FALLBACK_ENABLED = "fallback.enabled";

	private static final String TIMEOUT_ENABLED = "execution.timeout.enabled";

	private static final String TIMEOUT_MS = "execution.isolation.thread.timeoutInMilliseconds";

	@Autowired
	private SimpleServiceCaller simpleServiceCaller;

	@Override
	public void accept(ServiceCall serviceCall) {
		callWithHystrix(serviceCall);
	}

	@HystrixCommand(
			commandKey = "WebHookCall",
			fallbackMethod = "fallback",
			commandProperties = {
					@HystrixProperty(name = FALLBACK_ENABLED, value = "true"),
					@HystrixProperty(name = TIMEOUT_ENABLED, value = "true"),
					@HystrixProperty(name = TIMEOUT_MS, value = "20")
			})
	public void callWithHystrix(ServiceCall serviceCall) {
		simpleServiceCaller.call(serviceCall);
	}

	public void fallback(String callAddress) {
		logger.warn("failed to call subscription {}", callAddress);
	}

}
