package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.demo.data.domain.Member;

@Mapper
public interface MemberMapper {
	
	@Insert("INSERT INTO members ( name, email, password, createdAt, modifiedAt, del_yn)" + 
			"VALUES ( #{name}, #{email}, #{password}, NOW(), NOW(), 1)")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	//@SelectKey(statement="", keyProperty="id", before=true, resultType=Long.class)
	public Long insert(Member member);
	
	@Select("SELECT * FROM members WHERE name = #{name}")
	public Member findByName(String name);

}
