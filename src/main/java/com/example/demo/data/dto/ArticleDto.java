package com.example.demo.data.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ArticleDto {

	private String subject;
	private String content;

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleRequest extends ArticleDto {
		private Long memberId;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleUpdateRequest extends ArticleDto {
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ArticleResponse extends ArticleDto {

		private Long id;
		private Date regDate;
		private Integer readCount;
		private String memberName;

	}

}
