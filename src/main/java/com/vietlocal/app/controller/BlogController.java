package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.BlogPostResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.service.BlogService;
import com.vietlocal.app.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

	private final BlogService blogService;

	@GetMapping
	public ApiResponse<PageResponse<BlogPostResponse>> list(
			@PageableDefault(size = 20) Pageable pageable) {
		return ApiResponse.ok(blogService.findAll(pageable));
	}

	@GetMapping("/{slug}")
	public ApiResponse<BlogPostResponse> getBySlug(@PathVariable String slug) {
		return ApiResponse.ok(blogService.findBySlug(slug));
	}
}
