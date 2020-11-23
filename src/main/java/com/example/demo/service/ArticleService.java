package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.data.dto.ArticleDto.ArticleRequest;
import com.example.demo.data.dto.ArticleDto.ArticleResponse;
import com.example.demo.data.dto.ArticleDto.ArticleUpdateRequest;
import com.example.demo.data.vo.UserInfo;

public interface ArticleService {

	ArticleResponse getOne(Long id);

	Page<ArticleResponse> getList(Pageable pageable);

	ArticleResponse insert(ArticleRequest articleRequest, UserInfo user);

	void update(Long id, ArticleUpdateRequest articleUpdateRequest, UserInfo user);

	void delete(Long id, UserInfo user);

}