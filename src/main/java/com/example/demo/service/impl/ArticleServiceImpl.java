package com.example.demo.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.data.domain.Article;
import com.example.demo.data.dto.ArticleDto.ArticleRequest;
import com.example.demo.data.dto.ArticleDto.ArticleResponse;
import com.example.demo.data.dto.ArticleDto.ArticleUpdateRequest;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reopsitory.ArticleRepository;
import com.example.demo.service.ArticleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	private final ArticleRepository articleRepository;
	private final ModelMapper modelMapper;

	@Override
	public Page<ArticleResponse> getList(Pageable pageable) {
		return articleRepository.findAll(pageable).map(article -> modelMapper.map(article, ArticleResponse.class));
	}

	@Override
	public ArticleResponse getOne(Long id) {

		Article article = articleRepository.findById(id).orElseThrow(() -> {
			log.error("getOne {}", id);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		articleRepository.hitUp(id);

		return modelMapper.map(article, ArticleResponse.class);
	}

	@Override
	public ArticleResponse insert(ArticleRequest articleRequest, UserInfo user) {

		Article article = modelMapper.map(articleRequest, Article.class);

		if (!user.checkId(article.getMember().getId())) {
			log.error("insert {}", articleRequest, user.getUsername());
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		return modelMapper.map(articleRepository.save(article), ArticleResponse.class);
	}

	@Override
	public void update(Long id, ArticleUpdateRequest articleUpdateRequest, UserInfo user) {

		Article article = articleRepository.findOneById(id).orElseThrow(() -> {
			log.error("update {}", id, user.getUsername());
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		if (!user.checkId(article.getMember().getId())) {
			log.error("update {}", id, user.getUsername());
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		article.update(modelMapper.map(articleUpdateRequest, Article.class));
	}

	@Override
	public void delete(Long id, UserInfo user) {

		// 게시물 확인
		Article article = articleRepository.findOneById(id).orElseThrow(() -> {
			log.error("delete {}", id, user);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		// 권한 확인
		if (!user.checkId(article.getMember().getId())) {
			log.error("delete {}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		// 삭제 수행
		articleRepository.deleteById(id);
	}

}