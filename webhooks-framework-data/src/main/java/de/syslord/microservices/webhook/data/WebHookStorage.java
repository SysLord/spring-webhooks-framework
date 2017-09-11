package de.syslord.microservices.webhook.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.syslord.microservices.webhooks.storage.WebHookDto;
import de.syslord.microservices.webhooks.storage.WebHookException;
import de.syslord.microservices.webhooks.storage.interfaces.SecuredWebHookStorage;

@Component
public abstract class WebHookStorage implements SecuredWebHookStorage {

	private static final Logger logger = LoggerFactory.getLogger(WebHookStorage.class);

	@Autowired
	private WebHookEntityRepository webHookEntityRepository;

	@Autowired
	private WebHookEntityConverter entityconverter;

	@Override
	@Transactional
	public void add(String username, WebHookDto webHook) throws WebHookException {
		WebHookDto withOwner = webHook.withOwner(username);

		AbstractWebHookEntity entityWithOwner = entityconverter.toNewEntity(withOwner, createEntity());
		try {
			webHookEntityRepository.save(entityWithOwner);
		} catch (DataAccessException e) {
			Throwable mostSpecificCause = e.getMostSpecificCause();
			logger.error("", mostSpecificCause);
			throw new WebHookException(mostSpecificCause);
		}
	}

	protected abstract AbstractWebHookEntity createEntity();

	@Override
	@Transactional
	public void patch(String owner, WebHookDto newWebhook) throws WebHookException {

		AbstractWebHookEntity oldEntity = webHookEntityRepository.findByOwnerAndName(owner, newWebhook.getName());
		if (oldEntity == null) {
			throw new WebHookException("Kein Eintrag mit diesem Namen gefunden.");
		}
		Long oldId = oldEntity.getId();

		WebHookDto newWithOwner = newWebhook.withOwner(owner);

		AbstractWebHookEntity newEntity = entityconverter.toEntity(oldId, newWithOwner, createEntity());
		try {
			webHookEntityRepository.save(newEntity);
		} catch (DataAccessException e) {
			Throwable mostSpecificCause = e.getMostSpecificCause();
			logger.error("", mostSpecificCause);
			throw new WebHookException(mostSpecificCause);
		}
	}

	@Override
	@Transactional
	public void delete(String owner, String name) {
		try {
			webHookEntityRepository.deleteByOwnerAndName(owner, name);
		} catch (DataAccessException e) {
			logger.error("", e);
		}
	}

	@Override
	public List<WebHookDto> getWebHooksForUser(String username) {
		List<AbstractWebHookEntity> findByOwner = webHookEntityRepository.findByOwner(username);
		List<WebHookDto> list = entityconverter.toDto(findByOwner);
		return list;
	}

	@Override
	public List<WebHookDto> getWebHooksForEvent(String event) {
		List<AbstractWebHookEntity> findByEvent = webHookEntityRepository.findByEvent(event);
		return entityconverter.toDto(findByEvent);
	}

}
