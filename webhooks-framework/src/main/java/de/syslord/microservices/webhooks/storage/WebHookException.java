package de.syslord.microservices.webhooks.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "")
public class WebHookException extends Exception {

	private static final long serialVersionUID = -989484894351832971L;

	public WebHookException(String message) {
		super(message);
	}

	public WebHookException(Throwable throwable) {
		super(throwable);
	}

}
