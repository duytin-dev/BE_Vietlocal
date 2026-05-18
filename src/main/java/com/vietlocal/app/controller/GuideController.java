package com.vietlocal.app.controller;

import com.vietlocal.app.domain.enums.GuideTier;
import com.vietlocal.app.dto.response.GuideResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.service.GuideService;
import com.vietlocal.app.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class GuideController {

	private final GuideService guideService;

	@GetMapping
	public ApiResponse<PageResponse<GuideResponse>> list(
			@RequestParam(required = false) GuideTier tier,
			@PageableDefault(size = 20) Pageable pageable) {
		return ApiResponse.ok(guideService.findAll(tier, pageable));
	}

	@GetMapping("/{id}")
	public ApiResponse<GuideResponse> getById(@PathVariable Long id) {
		return ApiResponse.ok(guideService.findById(id));
	}
}
