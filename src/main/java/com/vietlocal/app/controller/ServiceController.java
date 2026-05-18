package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.ServiceResponse;
import com.vietlocal.app.service.ServiceOfferingService;
import com.vietlocal.app.utils.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

	private final ServiceOfferingService serviceOfferingService;

	@GetMapping
	public ApiResponse<List<ServiceResponse>> list() {
		return ApiResponse.ok(serviceOfferingService.findAll());
	}

	@GetMapping("/{id}")
	public ApiResponse<ServiceResponse> getById(@PathVariable Long id) {
		return ApiResponse.ok(serviceOfferingService.findById(id));
	}
}
