package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.DestinationResponse;
import com.vietlocal.app.security.AuthPrincipal;
import com.vietlocal.app.service.FavoriteService;
import com.vietlocal.app.utils.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@GetMapping
	public ApiResponse<List<DestinationResponse>> list(@AuthenticationPrincipal AuthPrincipal principal) {
		return ApiResponse.ok(favoriteService.listForUser(principal.userId()));
	}

	@PostMapping("/{destinationId}")
	public ApiResponse<Void> add(
			@PathVariable Long destinationId, @AuthenticationPrincipal AuthPrincipal principal) {
		favoriteService.add(principal.userId(), destinationId);
		return ApiResponse.ok("Added to favorites", null);
	}

	@DeleteMapping("/{destinationId}")
	public ApiResponse<Void> remove(
			@PathVariable Long destinationId, @AuthenticationPrincipal AuthPrincipal principal) {
		favoriteService.remove(principal.userId(), destinationId);
		return ApiResponse.ok("Removed from favorites", null);
	}
}
