package de.syslord.microservices.webhooks.testbase.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class WebHooksTestUserFactory {

	public static UserDetails createUser(String name, String commaSeparatedPermissions) {

		List<GrantedAuthority> grants = AuthorityUtils.commaSeparatedStringToAuthorityList(commaSeparatedPermissions);

		return createUser(name, name, grants);
	}

	private static User createUser(String name, String password, List<GrantedAuthority> grants) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		return new User(
				name,
				password,
				enabled,
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				grants);
	}
}
