package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.AuthDto.LoginRequest;
import com.example.demo.data.dto.AuthDto.TokenDto;
import com.example.demo.data.dto.ErrorResponse;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.SecurityService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "Authentication API.")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final SecurityService securityServiceImpl;

	@PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public TokenDto login(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		return securityServiceImpl.generateToken((UserDetails) authentication.getPrincipal());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AuthenticationException.class)
	protected ErrorResponse handleException(Exception e) {
		return ErrorResponse.of(ErrorCode.LOGIN_INPUT_INVALID);
	}

}
