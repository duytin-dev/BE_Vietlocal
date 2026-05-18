package com.vietlocal.app.controller;

import com.vietlocal.app.dto.request.CreateBookingRequest;
import com.vietlocal.app.dto.response.BookingNotificationResponse;
import com.vietlocal.app.dto.response.BookingResponse;
import com.vietlocal.app.dto.response.PaymentQrResponse;
import com.vietlocal.app.security.AuthPrincipal;
import com.vietlocal.app.service.BookingService;
import com.vietlocal.app.utils.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	public ApiResponse<BookingResponse> create(
			@Valid @RequestBody CreateBookingRequest request,
			@AuthenticationPrincipal AuthPrincipal principal) {
		Long userId = principal != null ? principal.userId() : null;
		return ApiResponse.ok("Booking created", bookingService.create(request, userId));
	}

	@GetMapping("/my")
	public ApiResponse<List<BookingNotificationResponse>> myBookings(
			@AuthenticationPrincipal AuthPrincipal principal) {
		return ApiResponse.ok(bookingService.findNotificationsForUser(principal.userId()));
	}

	@DeleteMapping("/{id}/notification")
	public ApiResponse<Void> dismissNotification(
			@PathVariable Long id, @AuthenticationPrincipal AuthPrincipal principal) {
		bookingService.dismissNotification(principal.userId(), id);
		return ApiResponse.ok("Removed from notifications", null);
	}

	@GetMapping("/{id}")
	public ApiResponse<BookingResponse> getById(@PathVariable Long id) {
		return ApiResponse.ok(bookingService.findById(id));
	}

	@GetMapping("/{id}/payment-qr")
	public ApiResponse<PaymentQrResponse> getPaymentQr(@PathVariable Long id) {
		return ApiResponse.ok(bookingService.getPaymentQr(id));
	}

	@PostMapping("/{id}/confirm-payment")
	public ApiResponse<PaymentQrResponse> confirmPayment(@PathVariable Long id) {
		return ApiResponse.ok("Payment confirmed", bookingService.confirmPayment(id));
	}
}
