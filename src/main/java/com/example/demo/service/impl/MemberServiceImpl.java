package com.example.demo.service.impl;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.data.domain.Member;
import com.example.demo.data.domain.Member.MemberBuilder;
import com.example.demo.data.domain.RoleCode;
import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberResponse;
import com.example.demo.data.dto.MemberDto.MemberUpdateRequest;
import com.example.demo.data.vo.UserVo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

	private final ModelMapper modelMapper;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Page<MemberResponse> getList(Pageable pageable) {
		return memberRepository.findAll(pageable).map(member -> modelMapper.map(member, MemberResponse.class));
	}

	@Override
	public MemberResponse insert(MemberRequest memberRequest) {

		if (memberRepository.existsByName(memberRequest.getName())) {
			log.info("insert() : memberRequest={}", memberRequest);
			throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
		}

		Member newMember = modelMapper.map(memberRequest, MemberBuilder.class)
				.password(passwordEncoder.encode(memberRequest.getPassword())).roles(Arrays.asList(RoleCode.USER))
				.build();

		return modelMapper.map(memberRepository.save(newMember), MemberResponse.class);
	}

	@Override
	public MemberResponse getOne(Long id, UserVo user) {

		Member member = memberRepository.findById(id).orElseThrow(() -> {
			log.info("getOne() : id={} , user={}", id, user);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		if (!user.checkMember(member)) {
			log.info("getOne() : id={} , user={}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		return modelMapper.map(member, MemberResponse.class);

	}

	@Override
	public void update(Long id, MemberUpdateRequest memberUpdateRequest, UserVo user) {

		if (user == null) {
			new BusinessException(ErrorCode.TOKEN_NOT_VALID);
		}

		Member member = memberRepository.findById(id).orElseThrow(() -> {
			log.info("update() : id={} , user={}", id, user);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		if (!user.checkMember(member)) {
			log.info("update() : id={} , user={}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		// dirty check
		member.update(modelMapper.map(memberUpdateRequest, MemberBuilder.class)
				.password(passwordEncoder.encode(memberUpdateRequest.getPassword())).build());
	}

	@Override
	public void delete(Long id, UserVo user) {

		if (user == null) {
			new BusinessException(ErrorCode.TOKEN_NOT_VALID);
		}

		Member member = memberRepository.findById(id).orElseThrow(() -> {
			log.info("delete() : id={} , user={} - ", id, user);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		if (user == null || !user.checkMember(member)) {
			log.error("delete() : id={} , user={}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		memberRepository.deleteById(id);
	}

}