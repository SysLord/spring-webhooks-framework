package de.syslord.microservices.webhooks.storage;

public class WebHookDtoBuilder {

	private String name;

	private String event;

	private String remoteAddress;

	private String postBody;

	private String username;

	private String password;

	public WebHookDtoBuilder(String name) {
		this.name = name;
	}

	public WebHookDtoBuilder withEvent(String event) {
		this.event = event;
		return this;
	}

	public WebHookDtoBuilder withREmoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
		return this;
	}

	public WebHookDtoBuilder withPostBody(String postBody) {
		this.postBody = postBody;
		return this;
	}

	public WebHookDtoBuilder withCredentials(String username, String password) {
		this.username = username;
		this.password = password;
		return this;
	}

	public WebHookDto build() {
		return new WebHookDto(
				name, event, remoteAddress,
				username, password,
				postBody,
				null);
	}

}
