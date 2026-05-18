package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.BrandStoryResponse;
import com.vietlocal.app.service.BrandStoryService;
import com.vietlocal.app.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/about")
@RequiredArgsConstructor
public class AboutController {

	private final BrandStoryService brandStoryService;

	@GetMapping
	public ApiResponse<BrandStoryResponse> getAbout() {
		return ApiResponse.ok(brandStoryService.getAbout());
	}
}
