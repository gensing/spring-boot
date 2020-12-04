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
import com.example.demo.data.vo.UserVo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reopsitory.ArticleRepository;
import com.example.demo.service.ArticleService;

import lombok.RequiredArgsConstructor;

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
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		articleRepository.hitUp(id);

		return modelMapper.map(article, ArticleResponse.class);
	}

	@Override
	public ArticleResponse insert(ArticleRequest articleRequest, UserVo user) {

		Article article = modelMapper.map(articleRequest, Article.class);

		if (!user.checkMember(article.getMember())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		return modelMapper.map(articleRepository.save(article), ArticleResponse.class);
	}

	@Override
	public void update(Long id, ArticleUpdateRequest articleUpdateRequest, UserVo user) {

		Article article = articleRepository.findOneById(id).orElseThrow(() -> {
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		if (!user.checkMember(article.getMember())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		article.update(modelMapper.map(articleUpdateRequest, Article.class));
	}

	@Override
	public void delete(Long id, UserVo user) {

		// 게시물 확인
		Article article = articleRepository.findOneById(id).orElseThrow(() -> {
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		// 권한 확인
		if (!user.checkMember(article.getMember())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		// 삭제 수행
		articleRepository.deleteById(id);
	}

}