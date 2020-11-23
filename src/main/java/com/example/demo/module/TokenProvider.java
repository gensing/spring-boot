package com.example.demo.module;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenProvider {

	private final byte[] secretKey;
	private final int jwtExpirationInMs;
	private final SignatureAlgorithm signatureAlgorithm;

	public TokenProvider(String secretKey, int jwtExpirationInMs, SignatureAlgorithm signatureAlgorithm) {
		this.secretKey = DatatypeConverter.parseBase64Binary(secretKey);
		this.jwtExpirationInMs = jwtExpirationInMs;
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public JwtBuilder getJwtBuilder() {

		Date now = new Date();
		Date end = new Date(now.getTime() + jwtExpirationInMs);

		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", signatureAlgorithm.getValue());
		header.put("regDate", now.getTime());

		return Jwts.builder().setHeader(header).setIssuedAt(now).setExpiration(end).signWith(signatureAlgorithm,
				new SecretKeySpec(this.secretKey, signatureAlgorithm.getJcaName()));
	}

//	에러를 낼 수 있음.
	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

}