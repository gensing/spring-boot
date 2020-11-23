package com.example.demo.reopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.data.domain.Article;

@Repository
public interface ArticleRepository
		extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, ArticleRepositoryCustom {

}
