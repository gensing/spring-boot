package com.example.demo.service.impl;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.data.domain.Member;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reopsitory.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByName(username)
				.orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_INPUT_INVALID));

		return new UserInfo(member.getId(), member.getName(), member.getPassword(), member.getRoles().stream()
				.map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
	}

}