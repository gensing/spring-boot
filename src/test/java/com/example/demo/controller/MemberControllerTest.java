package com.example.demo.controller;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberUpdateRequest;
import com.example.demo.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityService SecurityServiceImpl;

	@Test
	@Order(0)
	public void validTest() throws Exception {

		MemberRequest m = new MemberRequest();
		m.setEmail("gensing");
		m.setName("gensing");
		m.setPassword("1234");

		log.info("******** START : MOC MVC test **********");
		mvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new MemberRequest())))//
				.andExpect(status().isBadRequest())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))//
				.andDo(print());
		log.info("******** END : MOC MVC test **********");
	}

	@Test
	@Order(1)
	public void insertTest_1() throws Exception {

		MemberRequest firstMember = new MemberRequest();
		firstMember.setEmail("gensing@github.com");
		firstMember.setName("gensing");
		firstMember.setPassword("abc@12!4");

		log.info("******** START : MOC MVC test **********");
		mvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(firstMember)))//
				.andExpect(status().isCreated())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))//
				.andExpect(jsonPath("$.name").value("gensing"))//
				.andDo(print());
		log.info("******** END : MOC MVC test **********");

	}

	@Test
	@Order(2)
	public void getTest_2() throws Exception {

		String token = SecurityServiceImpl.generateToken(SecurityServiceImpl.loadUserByUsername("gensing"))
				.getAccessToken();

		log.info("******** START : MOC MVC test **********");
		mvc.perform(get("/member/{id}", 1).contentType(MediaType.APPLICATION_JSON).header("Authorization", token))//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))//
				.andExpect(jsonPath("$.name", is("gensing")))//
				.andDo(print());
		log.info("******** END : MOC MVC test **********");
	}

	@Test
	@Order(3)
	public void updateTest_3() throws Exception {

		String token = SecurityServiceImpl.generateToken(SecurityServiceImpl.loadUserByUsername("gensing"))
				.getAccessToken();

		MemberUpdateRequest updateRequest = new MemberUpdateRequest();
		updateRequest.setPassword("1@3@$dssdf");

		log.info("******** START : MOC MVC test **********");
		mvc.perform(put("/member/{id}", 1).contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
				.content(objectMapper.writeValueAsString(updateRequest)))//
				.andExpect(status().isNoContent())//
				.andDo(print());
		log.info("******** END : MOC MVC test **********");
	}

	@Test
	@Order(4)
	public void deleteTest_4() throws Exception {

		String token = SecurityServiceImpl.generateToken(SecurityServiceImpl.loadUserByUsername("gensing"))
				.getAccessToken();

		log.info("******** START : MOC MVC test **********");
		mvc.perform(delete("/member/{id}", 1).contentType(MediaType.APPLICATION_JSON).header("Authorization", token))//
				.andExpect(status().isNoContent())//
				.andDo(print());
		log.info("******** END : MOC MVC test **********");
	}

}
