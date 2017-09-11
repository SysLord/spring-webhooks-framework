package de.syslord.microservices.webhook.data;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.syslord.microservices.webhooks.storage.WebHookDto;

@Component
public class WebHookEntityConverter {

	public List<WebHookDto> toDto(List<AbstractWebHookEntity> webhooks) {
		return webhooks.stream()
			.map(w -> toDto(w))
			.collect(Collectors.toList());
	}

	public WebHookDto toDto(AbstractWebHookEntity webHook) {
		WebHookDto subscription = new WebHookDto(
				webHook.getName(),
				webHook.getEvent(),
				webHook.getRemoteAddress(),
				webHook.getUsername(),
				webHook.getPassword(),
				webHook.getPostBody(),
				webHook.getOwner());

		return subscription;
	}

	public AbstractWebHookEntity toEntity(Long id, WebHookDto webHook, AbstractWebHookEntity webHookEntity) {
		webHookEntity.setId(id);
		webHookEntity.setName(webHook.getName());
		webHookEntity.setEvent(webHook.getEvent());
		webHookEntity.setOwner(webHook.getOwner());
		webHookEntity.setRemoteAddress(webHook.getRemoteAddress());
		webHookEntity.setUsername(webHook.getUsername());
		webHookEntity.setPassword(webHook.getPassword());
		webHookEntity.setPostBody(webHook.getPostBody());

		return webHookEntity;
	}

	public AbstractWebHookEntity toNewEntity(WebHookDto webHook, AbstractWebHookEntity entity) {
		return toEntity(null, webHook, entity);
	}
}
