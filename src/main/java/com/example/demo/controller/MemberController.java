package com.example.demo.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.data.dto.MemberDto.MemberRequest;
import com.example.demo.data.dto.MemberDto.MemberResponse;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.service.MemberService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Member API.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

	private final MemberService memberService;

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MemberResponse> post(@Valid @RequestBody MemberRequest memberRequest) {
		MemberResponse memberResponse = memberService.insert(memberRequest);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(memberResponse.getId())
				.toUri();
		return ResponseEntity.created(uri).body(memberResponse);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MemberResponse get(@PathVariable Long id) {
		return memberService.getOne(id);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void put(@PathVariable Long id, @Valid @RequestBody MemberRequest memberRequest,
			@ApiIgnore @Valid @AuthenticationPrincipal UserInfo user) {
		memberService.update(id, memberRequest, user);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id, @ApiIgnore @Valid @AuthenticationPrincipal UserInfo user) {
		memberService.delete(id, user);
	}

}