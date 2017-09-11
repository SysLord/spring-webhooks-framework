package de.syslord.microservices.webhook.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebHookEntityRepository extends JpaRepository<AbstractWebHookEntity, Long> {

	List<AbstractWebHookEntity> findByOwner(String owner);

	List<AbstractWebHookEntity> findByEvent(String event);

	void deleteByOwnerAndName(String owner, String name);

	AbstractWebHookEntity findByOwnerAndName(String owner, String name);

}