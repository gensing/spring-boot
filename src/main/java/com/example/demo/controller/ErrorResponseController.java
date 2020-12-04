package com.example.demo.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.demo.data.dto.ErrorResponse;
import com.example.demo.data.dto.ErrorResponse.FieldError;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Slf4j
@RestController
@RestControllerAdvice
public class ErrorResponseController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status == null) {
			log.info("handleError()");
			return ResponseEntity.status(200).body(ErrorResponse.of(200, "error"));
		}

		int statusCode = Integer.valueOf(status.toString());
		String message = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		if (statusCode == HttpStatus.NOT_FOUND.value()) {
			log.info("handleError()");
			return ResponseEntity.status(statusCode).body(ErrorResponse.of(statusCode, "NOT FOUND"));
		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			log.error("handleError()");
			return ResponseEntity.status(statusCode).body(ErrorResponse.of(statusCode, "INTERNAL SERVER ERROR"));
		} else {
			log.info("handleError()");
			return ResponseEntity.status(statusCode).body(ErrorResponse.of(statusCode, message));
		}

	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		log.info("handleBusinessException() {}", e.getErrorCode().getMessage());
		final ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class,
			MethodArgumentTypeMismatchException.class })
	protected ErrorResponse handleBindException(Exception e) {

		if (e instanceof BindException) {
			log.info("handleBindException {}", e.getMessage());
			return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,
					FieldError.of(((BindException) e).getBindingResult()));
		}

		if (e instanceof MethodArgumentNotValidException) {
			log.info("handleMethodArgumentNotValidException {}", e.getMessage());
			return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,
					FieldError.of(((MethodArgumentNotValidException) e).getBindingResult()));
		}

		log.info("handleMethodArgumentTypeMismatchException {}", e.getMessage());
		MethodArgumentTypeMismatchException matme = (MethodArgumentTypeMismatchException) e;
		return ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE,
				FieldError.of(matme.getName(), String.valueOf(matme.getValue()), matme.getErrorCode()));
	}

	/**
	 * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
	 */
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		log.info("handleAccessDeniedException {}", e.getMessage());
		return ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
			HttpServletRequest request) {
		log.info("handleHttpRequestMethodNotSupportedException {}", e.getMessage());
		return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
	}

	/**
	 * 예상하지 못한 에러
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	protected ErrorResponse handleException(Exception e) {
		log.error("handleException() ", e);
		return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
	}

}