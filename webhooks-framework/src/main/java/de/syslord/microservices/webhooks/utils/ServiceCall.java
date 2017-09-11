package de.syslord.microservices.webhooks.utils;

import org.springframework.http.HttpEntity;

import de.syslord.microservices.webhooks.storage.WebHookCallMethod;

public class ServiceCall {

	private WebHookCallMethod httpMethod;

	private String address;

	private String username;

	private String password;

	private Object body;

	public ServiceCall(WebHookCallMethod httpMethod, String address,
			String username, String password,
			Object body) {
		this.httpMethod = httpMethod;
		this.address = address;
		this.username = username;
		this.password = password;
		this.body = body;
	}

	public WebHookCallMethod getHttpMethod() {
		return httpMethod;
	}

	public String getAddress() {
		return address;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Object getBody() {
		return body;
	}

	public boolean hasBasicAuth() {
		return username != null && !username.isEmpty() || password != null && !password.isEmpty();
	}

	public HttpEntity<?> buildHttpEntity() {
		HttpEntityBuilder entityBuilder = HttpEntityBuilder.create();

		if (hasBasicAuth()) {
			entityBuilder = entityBuilder.withBasicAuth(username, password);
		}

		if (body != null) {
			entityBuilder = entityBuilder.withBody(body);
		}

		return entityBuilder.build();
	}

}
