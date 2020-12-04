package com.example.demo.data.vo;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.demo.data.domain.Member;

import lombok.Getter;

@Getter
public class UserVo extends User {

	private static final long serialVersionUID = -3424911855654486191L;

	@NotNull(message = "not valid access token")
	private Long id;

	public UserVo(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	public boolean checkMember(Member member) {

		if (id == null || member == null || member.getId() == null) {
			return false;
		}

		return id.equals(member.getId());
	}

}