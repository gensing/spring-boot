package com.example.demo.reopsitory.impl;

import static com.example.demo.data.domain.QArticle.article;
import static com.example.demo.data.domain.QMember.member;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.demo.data.domain.Article;
import com.example.demo.reopsitory.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

	private final EntityManagerFactory emf;

	@Override
	public Optional<Article> findOneById(Long id) {
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(emf.createEntityManager());

		return Optional.of(jpaQueryFactory.select(article).from(article).join(article.member, member)
				.where(article.id.eq(id)).fetchOne());
	}

	@Override
	public void hitUp(Long id) {
		EntityManager em = emf.createEntityManager();
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		jpaQueryFactory.update(article).set(article.readCount, article.readCount.add(1)).where(article.id.eq(id))
				.execute();

		transaction.commit();
	}

	public List<Article> findAll(Pageable pageable) {
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(emf.createEntityManager());

		return jpaQueryFactory//
				.select(article)//
				.from(article)//
				.join(article.member, member)//
				.where(eqSubject(null))//
				.orderBy(article.createdAt.asc())//
				.offset(0).limit(10)//
				.fetch();
	}

	private BooleanExpression eqSubject(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return article.subject.eq(str);
	}

}