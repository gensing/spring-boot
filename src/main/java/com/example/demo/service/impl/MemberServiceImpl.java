package com.example.demo.service.impl;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.data.domain.Member;
import com.example.demo.data.domain.Role;
import com.example.demo.data.domain.Member.MemberBuilder;
import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberResponse;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
	public MemberResponse getOne(Long id) {
		return memberRepository.findById(id).map(member -> modelMapper.map(member, MemberResponse.class))
				.orElseThrow(() -> {
					log.error("getOne {}", id);
					return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
				});
	}

	@Override
	public MemberResponse insert(MemberRequest memberRequest) {

		if (memberRepository.existsByName(memberRequest.getName())) {
			log.error("insert {}", memberRequest);
			throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
		}

		MemberBuilder memberBuilder = modelMapper.map(memberRequest, MemberBuilder.class);
		memberBuilder.password(passwordEncoder.encode(memberRequest.getPassword()));
		memberBuilder.roles(Arrays.asList(Role.USER));

		Member newMember = memberRepository.save(memberBuilder.build());

		return modelMapper.map(newMember, MemberResponse.class);
	}

	@Override
	public void update(Long id, MemberRequest memberRequest, UserInfo user) {

		if (!user.checkId(id)) {
			log.error("update {}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		Member member = memberRepository.findById(id).orElseThrow(() -> {
			log.error("update {}", id, user);
			return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		});

		member.update(modelMapper.map(memberRequest, Member.class));
	}

	@Override
	public void delete(Long id, UserInfo user) {

		if (!user.checkId(id)) {
			log.error("delete {}", id, user);
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		if (!memberRepository.existsById(id)) {
			log.error("delete {}", id, user);
			throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		}

		memberRepository.deleteById(id);
	}

}