package com.example.demo.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.data.domain.Member;
import com.example.demo.data.dto.AuthDto.TokenDto;
import com.example.demo.data.vo.UserVo;
import com.example.demo.module.TokenProvider;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.service.SecurityService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("#handle() message={}", accessDeniedException.getMessage());
		response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("#commence() message={}", authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return new UserVo(member.getId(), member.getName(), member.getPassword(),
				member.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toList()));
	}

	@Override
	public TokenDto generateToken(UserDetails userDetails) {

		UserVo userVo = (UserVo) userDetails;

		Claims claims = Jwts.claims().setSubject("access");
		claims.put("id", userVo.getId());
		claims.put("username", userVo.getUsername());
		claims.put("role", userVo.getAuthorities());

		String accessToken = tokenProvider.generateToken(claims);
		

		return TokenDto.builder().id(userVo.getId()).username(userVo.getUsername()).accessToken(accessToken).build();
	}

	@Override
	public UserDetails getUserFromAccessToken(String accessToken) {

		try {
			Claims claims = tokenProvider.decodeToken(accessToken);

			Long id = claims.get("id", Long.class);
			String email = claims.get("username", String.class);
			Collection<?> role = claims.get("role", Collection.class);

			if (id == null || email == null || role == null) {
				log.info("Invalid JWT token");
				return null;
			}

			return new UserVo(id, email, "", AuthorityUtils
					.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString(role)));

		} catch (SignatureException ex) {
			log.info("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.info("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.info("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.info("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.info("JWT claims string is empty.");
		}

		return null;
	}

}