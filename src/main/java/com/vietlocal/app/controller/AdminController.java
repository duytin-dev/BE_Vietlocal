package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.AdminAiChatLogResponse;
import com.vietlocal.app.dto.response.AdminBookingResponse;
import com.vietlocal.app.dto.response.AdminDashboardResponse;
import com.vietlocal.app.dto.response.AdminUserResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.service.AdminService;
import com.vietlocal.app.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/dashboard")
	public ApiResponse<AdminDashboardResponse> dashboard() {
		return ApiResponse.ok(adminService.getDashboard());
	}

	@GetMapping("/bookings")
	public ApiResponse<PageResponse<AdminBookingResponse>> listBookings(
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ApiResponse.ok(adminService.listBookings(pageable));
	}

	@GetMapping("/users")
	public ApiResponse<PageResponse<AdminUserResponse>> listUsers(
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ApiResponse.ok(adminService.listUsers(pageable));
	}

	@GetMapping("/ai-chats")
	public ApiResponse<PageResponse<AdminAiChatLogResponse>> listAiChats(
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ApiResponse.ok(adminService.listAiChats(pageable));
	}
}
