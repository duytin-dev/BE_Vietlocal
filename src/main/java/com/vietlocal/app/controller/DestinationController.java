package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.DestinationResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.service.DestinationService;
import com.vietlocal.app.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

	private final DestinationService destinationService;

	@GetMapping
	public ApiResponse<PageResponse<DestinationResponse>> list(
			@PageableDefault(size = 20) Pageable pageable) {
		return ApiResponse.ok(destinationService.findAll(pageable));
	}

	@GetMapping("/{slug}")
	public ApiResponse<DestinationResponse> getBySlug(@PathVariable String slug) {
		return ApiResponse.ok(destinationService.findBySlug(slug));
	}
}
