package de.syslord.microservices.webhooksinclude.defaultservicecaller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.syslord.microservices.webhooks.storage.WebHookCallMethod;
import de.syslord.microservices.webhooks.utils.ServiceCall;

@Component
public class SimpleServiceCaller {

	@Autowired
	private RestTemplate restTemplate;

	public SimpleServiceCaller() {
		//
	}

	public SimpleServiceCaller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String call(ServiceCall serviceCall) {
		URI uri = UriComponentsBuilder
			.fromHttpUrl(serviceCall.getAddress())
			.build().toUri();

		HttpEntity<?> httpEntity = serviceCall.buildHttpEntity();

		return restTemplate.exchange(
				uri,
				WebHookCallMethod.POST.equals(serviceCall.getHttpMethod()) ? HttpMethod.POST : HttpMethod.GET,
				httpEntity,
				String.class)
			.getBody();
	}

}
