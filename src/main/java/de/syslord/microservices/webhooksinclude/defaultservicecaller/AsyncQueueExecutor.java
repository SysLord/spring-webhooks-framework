package de.syslord.microservices.webhooksinclude.defaultservicecaller;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.syslord.microservices.webhooks.events.interfaces.AsyncServiceCaller;
import de.syslord.microservices.webhooks.utils.SafeRunnable;
import de.syslord.microservices.webhooks.utils.ServiceCall;

@Component
public class AsyncQueueExecutor implements AsyncServiceCaller {

	private static final Logger logger = LoggerFactory.getLogger(AsyncQueueExecutor.class);

	private LinkedBlockingQueue<ServiceCall> queue = new LinkedBlockingQueue<>();

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Autowired
	private AsyncQueueExecutorConsumer asyncConsumer;

	@PostConstruct
	private void init() {
		scheduler.schedule(SafeRunnable.of(() -> readQueueForever()), 2, TimeUnit.SECONDS);
	}

	@PreDestroy
	private void teardown() {
		scheduler.shutdownNow();
	}

	@Override
	public void enqueue(ServiceCall t) {
		boolean added = queue.offer(t);
		if (!added) {
			logger.warn("queue is full!");
		}
	}

	private void readQueueForever() {
		while (!Thread.currentThread().isInterrupted()) {

			try {
				ServiceCall object = queue.take();
				asyncConsumer.accept(object);

			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
