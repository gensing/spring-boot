package com.example.demo.module;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class TokenProvider {

	private final int jwtExpirationInMs;
	private final SignatureAlgorithm signatureAlgorithm;
	private final Key key;

	public TokenProvider(String secret, SignatureAlgorithm signatureAlgorithm, int jwtExpirationInMs) {
		this.signatureAlgorithm = signatureAlgorithm;
		this.jwtExpirationInMs = jwtExpirationInMs;
		this.key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(secret), signatureAlgorithm.getJcaName());
	}

	public String generateToken(Claims claims) {

		Date now = new Date();

		return Jwts.builder()//
				.setHeaderParam("type", "JWT")//
				.setHeaderParam("alg", signatureAlgorithm)//
				.setIssuedAt(now)//
				.setExpiration(new Date(now.getTime() + jwtExpirationInMs))//
				.setClaims(claims)//
				.signWith(signatureAlgorithm, key)//
				.compact();
	}

	public Claims decodeToken(String token) throws SignatureException, MalformedJwtException, ExpiredJwtException,
			UnsupportedJwtException, IllegalArgumentException {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	public String extendToken(String token) throws SignatureException, MalformedJwtException, ExpiredJwtException,
			UnsupportedJwtException, IllegalArgumentException {

		return generateToken(decodeToken(token));
	}

}