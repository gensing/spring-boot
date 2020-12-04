package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.example.demo.data.dto.AuthDto.TokenDto;

public interface SecurityService extends UserDetailsService, AccessDeniedHandler, AuthenticationEntryPoint {

	public UserDetails getUserFromAccessToken(String token);

	public TokenDto generateToken(UserDetails userDetails);

}