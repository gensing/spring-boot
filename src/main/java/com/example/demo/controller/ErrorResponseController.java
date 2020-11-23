package com.example.demo.controller;

import java.util.List;

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

		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

		int status;
		ErrorResponse errorResponse;

		if (exception instanceof BusinessException) {
			ErrorCode errorCode = ((BusinessException) exception).getErrorCode();
			status = errorCode.getStatus();
			errorResponse = ErrorResponse.of(errorCode);
		} else {
			status = (int) request.getAttribute("javax.servlet.error.status_code");
			errorResponse = ErrorResponse.of(status, exception.getMessage());
		}

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class,
			MethodArgumentTypeMismatchException.class })
	protected ErrorResponse handleBindException(Exception e) {

		List<FieldError> fieldErrors = null;

		if (e instanceof BindException) {
			log.error("handleBindException", e);
			fieldErrors = FieldError.of(((BindException) e).getBindingResult());
		} else if (e instanceof MethodArgumentNotValidException) {
			log.error("handleMethodArgumentNotValidException", e);
			fieldErrors = FieldError.of(((MethodArgumentNotValidException) e).getBindingResult());
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			log.error("handleMethodArgumentTypeMismatchException", e);
			MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = (MethodArgumentTypeMismatchException) e;
			fieldErrors = FieldError.of(methodArgumentTypeMismatchException.getName(),
					String.valueOf(methodArgumentTypeMismatchException.getValue()),
					methodArgumentTypeMismatchException.getErrorCode());
		}

		return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
	}

	/**
	 * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
	 */
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		log.error("handleAccessDeniedException", e);
		return ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("handleHttpRequestMethodNotSupportedException", e);
		return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
	}

	/**
	 * 예상하지 못한 에러
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	protected ErrorResponse handleException(Exception e) {
		log.error("handleException", e);
		return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
	}

}