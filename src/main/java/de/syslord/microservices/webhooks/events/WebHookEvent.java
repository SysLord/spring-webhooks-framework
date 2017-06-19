package de.syslord.microservices.webhooks.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.syslord.microservices.webhooks.utils.JsonNoGetterAutodetect;

@JsonNoGetterAutodetect
public abstract class WebHookEvent {

	@JsonProperty("placeholders")
	private EventPlaceholders eventPlaceholder = new EventPlaceholders();

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private Object body = null;

	public EventPlaceholders getPlaceholder() {
		return eventPlaceholder;
	}

	protected void addPlaceholder(String param, String value) {
		eventPlaceholder.add(param, value);
	}

	public String applyPlaceholders(String stringWithPlaceholders) {
		return eventPlaceholder.apply(stringWithPlaceholders);
	}

	protected void setBody(Object body) {
		this.body = body;

	}

	public abstract String getName();

	public boolean hasBody() {
		return body != null;
	}

	public Object getBody() {
		return body;
	}

}
