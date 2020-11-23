package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberResponse;
import com.example.demo.data.vo.UserInfo;

public interface MemberService {

	MemberResponse getOne(Long id);

	Page<MemberResponse> getList(Pageable pageable);

	MemberResponse insert(MemberRequest memberRequest);

	void update(Long id, MemberRequest memberRequest, UserInfo user);

	void delete(Long id, UserInfo user);

}