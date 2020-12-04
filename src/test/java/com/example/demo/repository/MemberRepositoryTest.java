package com.example.demo.repository;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.config.JPAConfiguration;
import com.example.demo.data.domain.Member;
import com.example.demo.data.domain.RoleCode;
import com.example.demo.reopsitory.MemberRepository;

@Import(JPAConfiguration.class)
@ActiveProfiles("test")
@DataJpaTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	public void hello() throws Exception {

		// given
		Member newMember = Member.builder().name("gensing").email("gensing@gmail.com").password("1234")
				.roles(Arrays.asList(RoleCode.USER)).build();
		memberRepository.save(newMember);

		// when
		Member member = memberRepository.findAll().get(0);

		// then
		then("gensing").isEqualTo(member.getName());
	}

}