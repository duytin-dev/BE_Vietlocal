package com.vietlocal.app.controller;

import com.vietlocal.app.dto.request.LoginRequest;
import com.vietlocal.app.dto.request.RegisterRequest;
import com.vietlocal.app.dto.response.AuthResponse;
import com.vietlocal.app.dto.response.UserResponse;
import com.vietlocal.app.security.AuthPrincipal;
import com.vietlocal.app.service.AuthService;
import com.vietlocal.app.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ApiResponse.ok("Registered successfully", authService.register(request));
	}

	@PostMapping("/login")
	public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ApiResponse.ok("Logged in successfully", authService.login(request));
	}

	@GetMapping("/me")
	public ApiResponse<UserResponse> me(@AuthenticationPrincipal AuthPrincipal principal) {
		return ApiResponse.ok(authService.getProfile(principal.userId()));
	}
}
