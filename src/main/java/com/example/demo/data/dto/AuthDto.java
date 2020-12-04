package com.example.demo.data.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class AuthDto {

	@Data
	public static class LoginRequest {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
	}
	
	@Builder
	@AllArgsConstructor
	@Data
	public static class TokenDto {
		private String accessToken;
		private String refreshToken;
	}


	@Data
	public static class AccessTokenResponse {
		private String accessToken;
	}
}
