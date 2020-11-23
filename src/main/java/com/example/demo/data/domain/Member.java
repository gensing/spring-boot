package com.example.demo.data.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "members")
public class Member extends Common {

	@Column(unique = true, nullable = false)
	String name;

	@Column(unique = true, nullable = false)
	String email;

	@Column(nullable = false)
	String password;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "member_role", joinColumns = {
			@JoinColumn(name = "member_id") }, foreignKey = @ForeignKey(name = "fk_member_role_member_id"))
	@Column(name = "role_id", nullable = false)
	@Enumerated(EnumType.ORDINAL) // to converter
	List<Role> roles;

	public void update(Member member) {
		this.password = member.getPassword();
	}

}