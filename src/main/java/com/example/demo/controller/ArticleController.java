package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.example.demo.data.dto.ArticleDto.ArticleRequest;
import com.example.demo.data.dto.ArticleDto.ArticleResponse;
import com.example.demo.data.dto.ArticleDto.ArticleUpdateRequest;
import com.example.demo.data.vo.UserInfo;
import com.example.demo.service.ArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Article API.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = { "/article" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class ArticleController {

	private final ArticleService articleService;

	@ApiOperation(value = "get article list")
	@GetMapping("")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<ArticleResponse> gets(Pageable pageable) {
		return articleService.getList(pageable);
	}

	@ApiOperation(value = "get article")
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArticleResponse get(@PathVariable Long id) {
		return articleService.getOne(id);
	}

	@ApiOperation(value = "insert article")
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleResponse post(@Valid @RequestBody ArticleRequest articleRequest,
			@ApiIgnore @AuthenticationPrincipal UserInfo userInfo) {
		return articleService.insert(articleRequest, userInfo);
	}

	@ApiOperation(value = "update article")
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void put(@PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest articleUpdateRequest,
			@ApiIgnore @AuthenticationPrincipal UserInfo userInfo) {
		articleService.update(id, articleUpdateRequest, userInfo);
	}

	@ApiOperation(value = "delete article")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, @ApiIgnore @AuthenticationPrincipal UserInfo userInfo) {
		articleService.delete(id, userInfo);
	}

}