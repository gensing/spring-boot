package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Constant;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

	private final TokenService tokenServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader(Constant.tokenName);

		if (StringUtils.hasText(token)) {
			UserInfo userInfo = tokenServiceImpl.decodeToken(token);
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities()));
		}

		filterChain.doFilter(request, response);
	}

}