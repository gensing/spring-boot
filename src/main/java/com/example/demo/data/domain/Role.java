package com.example.demo.data.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ADMIN(0, "ROLE_ADMIN"), 
	USER(1, "ROLE_USER");

	private int code;
	private String name;
}