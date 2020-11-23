package com.example.demo.reopsitory;

import java.util.Optional;

import com.example.demo.data.domain.Article;

public interface ArticleRepositoryCustom {
	public void hitUp(Long id);

	public Optional<Article> findOneById(Long id);
}