package de.syslord.microservices.webhook.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public abstract class AbstractWebHookEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String name;

	@NotNull
	@Column(nullable = false)
	private String event;

	@NotNull
	@Column(nullable = false)
	private String owner;

	@NotNull
	@Column(nullable = false)
	private String remoteAddress;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String postBody;

	public AbstractWebHookEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPostBody() {
		return postBody;
	}

	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}

}