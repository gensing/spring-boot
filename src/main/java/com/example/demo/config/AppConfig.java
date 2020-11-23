package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.module.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expirationInMs}")
	private int jwtExpirationInMs;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns("/user/**");
	}

	@Bean
	public TokenProvider jwtProvider() {
		return new TokenProvider(secret, jwtExpirationInMs, SignatureAlgorithm.HS512);
	}

	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}

	@Bean
	public ModelMapper modelmapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE).setFieldMatchingEnabled(true)
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper ojbectMapper = new ObjectMapper();
		return ojbectMapper;
	}
}
