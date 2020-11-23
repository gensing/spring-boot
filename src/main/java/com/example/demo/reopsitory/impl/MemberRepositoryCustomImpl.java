package com.example.demo.reopsitory.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.demo.data.domain.Member;
import com.example.demo.reopsitory.MemberRepositoryCustom;;

public class MemberRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

	public MemberRepositoryCustomImpl() {
		super(Member.class);
	}

}