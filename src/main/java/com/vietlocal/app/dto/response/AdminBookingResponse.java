package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.BookingStatus;
import com.vietlocal.app.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminBookingResponse {
	private Long id;
	private String customerName;
	private String email;
	private String phone;
	private Long guideId;
	private String guideName;
	private Long userId;
	private String userFullName;
	private String destinationName;
	private String tripTitle;
	private String itinerarySummary;
	private String customerNotes;
	private int estimatedDays;
	private BigDecimal totalAmount;
	private BookingStatus status;
	private Instant createdAt;
	private PaymentStatus paymentStatus;
	private String transactionRef;
}
