package com.example.demo.data.dto;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class PageRequest {
	
	//https://www.baeldung.com/javax-validations-enums

	private int page;
	private int size;
	private Sort.Direction direction;

	public org.springframework.data.domain.PageRequest of() {
		defaultInit();
		return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "id");
	}

	private void defaultInit() {

		final int DEFAULT_PAGE = 1;
		final int DEFAULT_SIZE = 10;
		final int MAX_SIZE = 50;
		final Sort.Direction DEFAULT_DIRECTION = Direction.ASC;

		this.page = page <= 0 ? DEFAULT_PAGE : page;
		this.size = size <= 0 || size > MAX_SIZE ? DEFAULT_SIZE : size;
		this.direction = direction == null ? DEFAULT_DIRECTION : direction;
	}
}