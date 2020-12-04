package com.example.demo.data.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ArticleDto {

	@ApiModelProperty(required = true)
	@NotBlank(message = "제목은 필수 입력 값입니다.")
	private String subject;

	@ApiModelProperty(required = true)
	@NotBlank(message = "내용은 필수 입력 값입니다.")
	private String content;

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleRequest extends ArticleDto {

		@ApiModelProperty(required = true, value = "id")
		private Long memberId;
	}

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleUpdateRequest extends ArticleDto {
	}

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleResponse extends ArticleDto {

		private Long id;
		private Date regDate;
		private Integer readCount;
		private String memberName;

	}

}
