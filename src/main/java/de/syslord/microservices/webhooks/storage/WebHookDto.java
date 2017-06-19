package de.syslord.microservices.webhooks.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.syslord.microservices.webhooks.utils.JsonNoGetterAutodetect;
import de.syslord.microservices.webhooks.utils.JsonPasswordSerializer;

@JsonNoGetterAutodetect
@JsonPropertyOrder(value = { "name", "event", "remoteAddress" })

public class WebHookDto {

	@JsonProperty(value = "name", required = true)
	private String name;

	@JsonProperty(value = "event", required = true)
	private String event;

	@JsonProperty(value = "remoteAddress", required = true)
	private String remoteAddress;

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	@JsonSerialize(using = JsonPasswordSerializer.class)
	private String password;

	@JsonProperty("postBody")
	private String postBody;

	@JsonIgnore
	private String owner;

	public WebHookDto() {
		// deserialization constructor
	}

	public WebHookDto(String name, String event, String remoteAddress, String username, String password,
			String postBody,
			String owner) {
		this.name = name;
		this.event = event;
		this.remoteAddress = remoteAddress;
		this.username = username;
		this.password = password;
		this.postBody = postBody;
		this.owner = owner;
	}

	public static WebHookDtoBuilder builder(String name) {
		return new WebHookDtoBuilder(name);
	}

	public WebHookDto withOwner(String newOwner) {
		WebHookDto webHookDto = new WebHookDto();
		webHookDto.owner = newOwner;
		webHookDto.event = event;
		webHookDto.name = name;
		webHookDto.remoteAddress = remoteAddress;
		webHookDto.username = username;
		webHookDto.password = password;
		webHookDto.postBody = postBody;
		return webHookDto;
	}

	public String getOwner() {
		return owner;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public boolean matchesEvent(String eventname) {
		return event.equals(eventname);
	}

	public String getName() {
		return name;
	}

	public String getEvent() {
		return event;
	}

	public String getPostBody() {
		return postBody;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public WebHookCallMethod getHttpMethod() {
		return hasPostBody() ? WebHookCallMethod.POST : WebHookCallMethod.GET;
	}

	public boolean hasPostBody() {
		return postBody != null && !postBody.isEmpty();
	}

}
