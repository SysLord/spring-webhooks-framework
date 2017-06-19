package de.syslord.microservices.webhooks.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import de.syslord.microservices.webhooks.events.Events;
import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.interfaces.WebHookStorage;
import de.syslord.microservices.webhooks.test.events.ExampleEventX;
import de.syslord.microservices.webhooks.test.events.ExampleEventY;
import de.syslord.microservices.webhooks.test.events.ExampleEventZ;
import de.syslord.microservices.webhooks.testbase.ServiceCallCollector;
import de.syslord.microservices.webhooks.utils.HttpEntityBuilder;
import io.restassured.path.json.JsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = { WebHookTestConfig.class },
		webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebHooksTest {

	private static final Logger logger = LoggerFactory.getLogger(WebHooksTest.class);

	@LocalServerPort
	private int port;

	@Autowired
	private Events events;

	@Autowired
	private WebHookStorage webHookStorage;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceCallCollector serviceCallCollector;

	@Before
	public void setup() {
		events.clear();
		events.addEventType(ExampleEventX.NAME, () -> ExampleEventX.create("testParameter"));
		events.addEventType(ExampleEventY.NAME, () -> ExampleEventX.create("testParameter"));
		events.addEventType(ExampleEventZ.NAME, () -> ExampleEventX.create("testParameter"));

		serviceCallCollector.reset();
	}

	@Test
	public void contextLoads() throws Exception {
		//
	}

	private String getUrl(String endpoint) {
		return "http://localhost:" + port + endpoint;
	}

	private String getUrl(String endpoint, String parameters) {
		return "http://localhost:" + port + endpoint + "?" + parameters;
	}

	@Test
	public void testGetEvents_ReturnsOnlyPermittedEvents() throws Exception {

		String url = getUrl(WebHookTestEndpoints.GET_EVENTS);
		logger.info("events url {}", url);

		HttpEntity<?> httpEntity = HttpEntityBuilder.create()
			.withBasicAuth(WebHookTestConfig.TESTUSER_A, WebHookTestConfig.TESTUSER_A)
			.build();
		String json = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
		logger.info("json {}", json);

		// expected:
		// "[{\"eventname\":\"X\",\"exampleValues\":{\"placeholders\":{\"{parameter}\":\"testParameter\"}}}]";
		JsonPath jsonPath = JsonPath.from(json);
		assertEquals(1, jsonPath.getList("$").size());
		assertEquals("X", jsonPath.get("[0].eventname"));
		assertEquals("testParameter", jsonPath.getMap("[0].exampleValues.placeholders").get("{parameter}"));
	}

	@Test
	public void testRegisterWebHook() throws Exception {
		WebHookDto hook = WebHookDto.builder("MyHook")
			.withEvent(ExampleEventX.NAME)
			.withREmoteAddress(getUrl(WebHookTestEndpoints.CALL_ME_BY_HOOK, "myParameter={parameter}"))
			.withCredentials(WebHookTestConfig.CALLME_ENDPOINT_USER, WebHookTestConfig.CALLME_ENDPOINT_USER)
			.build();

		webHookStorage.add(WebHookTestConfig.TESTUSER_A, hook);

		ExampleEventX exampleEvent = ExampleEventX.create("konkreterEventParameter");
		events.fireEvent(exampleEvent);

		serviceCallCollector.logCallList();

		serviceCallCollector.verify(
				1,
				getUrl(WebHookTestEndpoints.CALL_ME_BY_HOOK),
				"myParameter=konkreterEventParameter",
				WebHookTestConfig.CALLME_ENDPOINT_USER);
	}

}
