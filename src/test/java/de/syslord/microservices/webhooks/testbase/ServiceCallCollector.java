package de.syslord.microservices.webhooks.testbase;

import static org.junit.Assert.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallCollector {

	private static final Logger logger = LoggerFactory.getLogger(ServiceCallCollector.class);

	List<Triplet<String, String, String>> callList = Lists.newArrayList();

	public void verify(int callCount, String url, String username) {
		assertEquals(callCount, callList.size());

		boolean present = callList.stream()
			.filter(tr -> url.equals(tr.getValue0()) && username.equals(tr.getValue2()))
			.findFirst()
			.isPresent();

		assertTrue(present);
	}

	public void verify(int callCount, String url, String parameters, String username) {
		assertEquals(callCount, callList.size());

		boolean present = callList.stream()
			.filter(tr -> url.equals(tr.getValue0())
					&& parameters.equals(tr.getValue1())
					&& username.equals(tr.getValue2()))
			.findFirst()
			.isPresent();

		assertTrue(present);
	}

	public void reset() {
		callList.clear();
	}

	public void called(String url, String queryString, String username) {
		callList.add(new Triplet<>(url, queryString, username));
		logger.info("Call: {} {} {}", url, queryString, username);
	}

	public void logCallList() {
		logger.info("calls " + callList);
	}

}
