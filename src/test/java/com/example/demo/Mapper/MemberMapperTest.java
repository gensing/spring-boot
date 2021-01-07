package com.example.demo.Mapper;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.config.JPAConfiguration;
import com.example.demo.config.MybatisConfiguration;
import com.example.demo.data.domain.Member;
import com.example.demo.mapper.MemberMapper;

@Import({MybatisConfiguration.class,JPAConfiguration.class})
@ActiveProfiles("test")
@DataJpaTest
public class MemberMapperTest {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@BeforeEach
	public void setUp() {
		Member member = Member.builder().email("test@gmail.com").name("test").password("test").build();
		memberMapper.insert(member);
	}

	@Test
	public void hello() throws Exception {
		
		Member member = memberMapper.findByName("test");
		then("test").isEqualTo(member.getName());

	}
}
