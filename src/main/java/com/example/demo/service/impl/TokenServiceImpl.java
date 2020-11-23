package com.example.demo.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.data.vo.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.module.TokenProvider;
import com.example.demo.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

	private final TokenProvider TokenProvider;

	@Override
	public String generateToken(UserInfo userInfo) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userInfo.getId());
		claims.put("email", userInfo.getUsername());
		claims.put("role", userInfo.getAuthorities());

		return TokenProvider.getJwtBuilder().setSubject(userInfo.getUsername()).setClaims(claims).compact();
	}

	@Override
	public UserInfo decodeToken(String token) {

		try {
			Claims claims = TokenProvider.getClaims(token);

			String email = claims.get("email", String.class);
			Long id = claims.get("id", Long.class);
			Object authorities = claims.get("role");

			List<GrantedAuthority> roles = null;

			if (authorities instanceof Collection)
				roles = AuthorityUtils.commaSeparatedStringToAuthorityList(
						StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));

			return new UserInfo(id, email, "", roles);

		} catch (ExpiredJwtException exception) {
			log.info("Token Expired", exception.getClaims().getSubject());
		} catch (JwtException exception) {
			log.info("Token Tampered");
		} catch (NullPointerException exception) {
			log.info("Token is null");
		}

		throw new BusinessException(ErrorCode.TOKEN_NOT_VALID);
	}

}