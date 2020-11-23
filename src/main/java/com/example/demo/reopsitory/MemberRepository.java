package com.example.demo.reopsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.demo.data.domain.Member;

//https:www.popit.kr/spring-boot-jpa-step-15-querydsl%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4%EC%84%9C-repository-%ED%99%95%EC%9E%A5%ED%95%98%EA%B8%B0-2/
//https://stackoverflow.com/questions/18300465/spring-data-jpa-and-querydsl-to-fetch-subset-of-columns-using-bean-constructor-p
// JpaRepository는   Custom으로 끝나는 이름의 인터페이스를 상속할 수 있으면 ->  CustomImpl로 끝나는 구현체를 추가한다.

public interface MemberRepository
		extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {
	public Optional<Member> findByName(String name);
	// @Modifying
	// @Transactional

	boolean existsByName(String name);

}
