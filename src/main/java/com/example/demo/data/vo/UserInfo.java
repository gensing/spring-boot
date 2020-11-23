package com.example.demo.data.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserInfo extends User {

	private static final long serialVersionUID = -3424911855654486191L;
	private Long id;

	public UserInfo(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	public boolean checkId(Long id) {
		if (id == null) {
			return false;
		}
		return id.equals(this.getId());
	}

}
