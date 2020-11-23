package com.example.demo.data.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class MemberDto {

	private String name;
	private String email;

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MemberRequest extends MemberDto {
		private String password;

		public void encodingPassword(PasswordEncoder passwordEncoder) {
			this.password = passwordEncoder.encode(this.password);
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MemberResponse extends MemberDto {
		private Long id;
	}
}