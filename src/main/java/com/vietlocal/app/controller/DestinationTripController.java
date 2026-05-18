package com.vietlocal.app.controller;

import com.vietlocal.app.dto.response.DestinationTripResponse;
import com.vietlocal.app.service.DestinationTripService;
import com.vietlocal.app.utils.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destination-trips")
@RequiredArgsConstructor
public class DestinationTripController {

	private final DestinationTripService destinationTripService;

	@GetMapping
	public ApiResponse<List<DestinationTripResponse>> list() {
		return ApiResponse.ok(destinationTripService.findAll());
	}
}
