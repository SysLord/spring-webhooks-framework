package de.syslord.microservices.webhooks.utils;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.google.common.collect.Maps;

public class HttpEntityBuilder {

	protected static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

	private Map<String, String> headersMap = Maps.newHashMap();

	private Object body;

	private boolean withBasicAuth = false;

	private String username;

	private String password;

	public static HttpEntityBuilder create() {
		return new HttpEntityBuilder();
	}

	public HttpEntityBuilder withBasicAuth(String username, String password) {
		this.username = username;
		this.password = password;
		withBasicAuth = true;

		return this;
	}

	public HttpEntityBuilder withBody(Object body) {
		this.body = body;
		return this;
	}

	public HttpEntityBuilder withHeader(String name, String value) {
		headersMap.put(name, value);
		return this;
	}

	public HttpEntity<?> build() {
		HttpHeaders headers = new HttpHeaders();
		headersMap.entrySet()
			.forEach(entry -> headers.set(entry.getKey(), entry.getValue()));

		if (withBasicAuth) {
			headers.set(HTTP_HEADER_AUTHORIZATION, generateBasicAuth(username, password));
		}

		HttpEntity<?> entity;
		if (body != null) {
			entity = new HttpEntity<>(body, headers);
		} else {
			entity = new HttpEntity<>(headers);
		}

		return entity;
	}

	private String generateBasicAuth(String name, String pw) {
		String auth = name + ":" + pw;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("UTF-8")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

}
