package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.vo.UserInfo;
import com.example.demo.service.TokenService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "Authentication API.")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenProvider;

	@PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public String login(String username, String password) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return String.format("{ \"token\" : \"%s\"}",
				tokenProvider.generateToken((UserInfo) authentication.getPrincipal()));
	}

}
