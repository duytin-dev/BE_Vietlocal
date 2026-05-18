package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingNotificationResponse {
	private Long bookingId;
	private String customerName;
	private String tripName;
	private BigDecimal totalAmount;
	private PaymentStatus paymentStatus;
	private Instant createdAt;
}
