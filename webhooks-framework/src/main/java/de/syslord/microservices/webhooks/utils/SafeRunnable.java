package de.syslord.microservices.webhooks.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When a task of an ExecutorService throws an exception the scheduler just stops without any obvious signs.
 * This can be used to wrap the task. Exceptions are logged and the scheduler will continue;
 */
public class SafeRunnable {

	private static final Logger logger = LoggerFactory.getLogger(SafeRunnable.class);

	public static Runnable of(Runnable unsafe) {
		return () -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					unsafe.run();
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		};
	}
}
