package de.syslord.microservices.webhooks.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.syslord.microservices.webhooks.utils.JsonNoGetterAutodetect;

@JsonNoGetterAutodetect
public class WebHookRegistrationsDto {

	@JsonProperty("WebHooks")
	private Map<String, WebHookDto> nameWebHooks = new HashMap<>();

	public WebHookRegistrationsDto() {
		// deserialization constructor
	}

	public WebHookRegistrationsDto(List<WebHookDto> webHooks) {
		this.nameWebHooks = webHooks.stream()
			.collect(Collectors.toMap(
					s -> s.getName(),
					s -> s));
	}

	public static WebHookRegistrationsDto createEmpty() {
		return new WebHookRegistrationsDto();
	}

}
