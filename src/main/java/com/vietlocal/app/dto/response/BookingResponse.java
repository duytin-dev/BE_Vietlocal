package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {
	private Long id;
	private String customerName;
	private String email;
	private String phone;
	private Long guideId;
	private String guideName;
	private String destinationName;
	private String tripTitle;
	private String itinerarySummary;
	private String customerNotes;
	private int estimatedDays;
	private BigDecimal totalAmount;
	private BookingStatus status;
	private Instant createdAt;
}
