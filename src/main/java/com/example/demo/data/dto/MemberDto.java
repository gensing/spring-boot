package com.example.demo.data.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
public class MemberDto {

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(min = 2, max = 10)
	private String name;

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String email;

	@ApiModel
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class MemberRequest extends MemberDto {

		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
		private String password;

	}

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MemberUpdateRequest {

		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
		private String password;

	}

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MemberResponse extends MemberDto {
		private Long id;
	}
}