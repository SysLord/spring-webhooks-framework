package de.syslord.microservices.webhooks.storage.interfaces;

import java.util.List;

import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.WebHookException;

public interface WebHookStorage {

	void add(String username, WebHookDto webHookDto) throws WebHookException;

	void patch(String username, WebHookDto webHookDto) throws WebHookException;

	void delete(String username, String id);

	List<WebHookDto> getWebHooksForUser(String username);

	List<WebHookDto> getWebHooksForEvent(String eventname);

}
