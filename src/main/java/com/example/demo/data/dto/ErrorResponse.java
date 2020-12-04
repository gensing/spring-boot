package com.example.demo.data.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.example.demo.exception.ErrorCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	@ApiModelProperty(position = 1, notes = "상태")
	private int status;
	@ApiModelProperty(position = 2, notes = "에러 코드")
	private String code;
	@ApiModelProperty(position = 3, notes = "메세지")
	private String message;
	@ApiModelProperty(position = 4, notes = "필드 에러")
	private List<FieldError> errors;

	private ErrorResponse(final int stauts, final String message) {
		this.status = stauts;
		this.code = null;
		this.message = message;
		this.errors = new ArrayList<>();
	}

	private ErrorResponse(final ErrorCode code) {
		this.status = code.getStatus();
		this.code = code.getCode();
		this.message = code.getMessage();
		this.errors = new ArrayList<>();
	}

	private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
		this.status = code.getStatus();
		this.code = code.getCode();
		this.message = code.getMessage();
		this.errors = errors;
	}

	public static ErrorResponse of(final int stauts, final String message) {
		return new ErrorResponse(stauts, message);
	}

	public static ErrorResponse of(final ErrorCode code) {
		return new ErrorResponse(code);
	}

	public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
		return new ErrorResponse(code, errors);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FieldError {
		private String field;
		private String value;
		private String reason;

		private FieldError(final String field, final String value, final String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<FieldError> of(final String field, final String value, final String reason) {
			return Arrays.asList(new FieldError(field, value, reason));
		}

		public static List<FieldError> of(final BindingResult bindingResult) {
			return bindingResult
					.getFieldErrors().stream().map(error -> new FieldError(error.getField(),
							String.valueOf(error.getRejectedValue()), error.getDefaultMessage()))
					.collect(Collectors.toList());
		}
	}

}
