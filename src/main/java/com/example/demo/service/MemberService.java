package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberResponse;
import com.example.demo.data.dto.MemberDto.MemberUpdateRequest;
import com.example.demo.data.vo.UserVo;

public interface MemberService {
	Page<MemberResponse> getList(Pageable pageable);

	MemberResponse insert(MemberRequest memberRequest);

	MemberResponse getOne(Long id, UserVo user);

	void update(Long id, MemberUpdateRequest memberUpdateRequest, UserVo user);

	void delete(Long id, UserVo user);

}