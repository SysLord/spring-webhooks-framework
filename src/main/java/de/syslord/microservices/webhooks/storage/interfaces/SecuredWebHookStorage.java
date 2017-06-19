package de.syslord.microservices.webhooks.storage.interfaces;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.WebHookException;

public interface SecuredWebHookStorage extends WebHookStorage {

	@Override
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && @eventSecurity.hasEventPermission(#webHookDto.event)")
	void add(String username, WebHookDto webHookDto) throws WebHookException;

	@Override
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && @eventSecurity.hasEventPermission(#webHookDto.event)")
	void patch(String username, WebHookDto webHookDto) throws WebHookException;

	@Override
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER')")
	void delete(String username, String id);

	@Override
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER')")
	List<WebHookDto> getWebHooksForUser(String username);

	@Override
	List<WebHookDto> getWebHooksForEvent(String eventname);

}
