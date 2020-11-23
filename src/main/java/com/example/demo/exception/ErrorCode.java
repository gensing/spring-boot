package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
	// https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C
	// Common
	INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"), //
	INVALID_TYPE_VALUE(400, "C002", " Invalid Type Value"), //
	HANDLE_ACCESS_DENIED(403, "C003", "Access is Denied"), //
	ENTITY_NOT_FOUND(404, "C004", " Entity Not Found"), //
	METHOD_NOT_ALLOWED(405, "C005", " Method Not Allowed"), //
	INTERNAL_SERVER_ERROR(500, "C006", "INTERNAL_SERVER_ERROR"), //

	// Member
	EMAIL_DUPLICATION(400, "M001", "Email is Duplication"), //
	LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"), //

	// Token
	TOKEN_NOT_VALID(400, "T001", "Token is not valid");

	private final int status;
	private final String code;
	private final String message;

}