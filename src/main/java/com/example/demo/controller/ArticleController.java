package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.example.demo.data.dto.PageRequest;
import com.example.demo.data.vo.UserVo;
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

	private final ArticleService articleServiceImpl;

	@ApiOperation(value = "get article list")
	@GetMapping(value = "")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<ArticleResponse> gets(@ModelAttribute PageRequest pageRequest) {
		return articleServiceImpl.getList(pageRequest.of());
	}

	@ApiOperation(value = "get article")
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArticleResponse get(@PathVariable Long id) {
		return articleServiceImpl.getOne(id);
	}

	@ApiOperation(value = "insert article")
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleResponse post(@Valid @RequestBody ArticleRequest articleRequest,
			@ApiIgnore @AuthenticationPrincipal UserVo userInfo) {
		return articleServiceImpl.insert(articleRequest, userInfo);
	}

	@ApiOperation(value = "update article")
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void put(@PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest articleUpdateRequest,
			@ApiIgnore @AuthenticationPrincipal UserVo userInfo) {
		articleServiceImpl.update(id, articleUpdateRequest, userInfo);
	}

	@ApiOperation(value = "delete article")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, @ApiIgnore @AuthenticationPrincipal UserVo userInfo) {
		articleServiceImpl.delete(id, userInfo);
	}

}